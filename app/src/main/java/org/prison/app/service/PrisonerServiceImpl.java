package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Degree;
import org.prison.model.data.edu.Enrollment;
import org.prison.model.data.edu.PrisonerDegree;
import org.prison.model.data.prisoners.Communication;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.*;
import org.prison.model.repositories.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Service
@Slf4j
public class PrisonerServiceImpl implements PrisonerService {

    private final PrisonerRepository prisonerRepository;
    private final PrisonerDegreeRepository prisonerDegreeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CommunicationRepository communicationRepository;

    private static final String PRISONER_NOT_FOUND = "Prisoner with id=%d Not Found";
    public static final String PRISONER_STATUS_NOT_FOUND = "Prisoner status with name=%s Not Found";
    public static final String COMMUNICATION_NOT_FOUND = "Communication with id=%d NOT FOUND";
    public static final String STATUS_NOT_FOUND = "Status with name=%s Not Found";

    @Override
    public Prisoner findById(int id) {
        return prisonerRepository.findById(id).orElseThrow(() -> {
                    String message = PRISONER_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                }
        );
    }

    @Override
    @Transactional
    public Prisoner savePrisoner(int deptId, Prisoner prisoner) {
        prisoner = prisonerRepository.save(prisoner);
        prisonerRepository.assignPrisonerToDepartment(deptId, prisoner.getId());
        return prisoner;
    }

    @Override
    public Prisoner savePrisoner(Prisoner prisoner) {
        return prisonerRepository.save(prisoner);
    }

    @Override
    public void deletePrisonerById(int id) {
        prisonerRepository.deleteById(id);
    }

    @Override
    public List<Prisoner> findAllPrisoners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Prisoner> slice = prisonerRepository.findAllPrisoners(pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    @Transactional
    public StatisticsResp getStatistics(StatisticsReq statReq) {
        StatisticsResp statisticsResp = new StatisticsResp();

        /* Crime Type Stat */
        if (statReq.getCrimeType() != null) {
            statisticsResp.setCrimeTypeStat(prisonerRepository
                    .statisticsByCrimeType(statReq.getCrimeType())
                    .stream()
                    .collect(Collectors.toMap((StringProjection::getString),
                            StringProjection::getCount)));
        }

        /* Age Stat */
        if (statReq.getAgeStart() != null && statReq.getAgeEnd() != null) {
            if (statReq.getAgeEnd() == 0) statReq.setAgeEnd(statReq.getAgeStart());
            statisticsResp.setAgeStat(prisonerRepository
                    .statisticsByAge(statReq.getAgeStart(), statReq.getAgeEnd())
                    .stream()
                    .collect(Collectors.toMap((IntegerProjection::getAge),
                            IntegerProjection::getCount)));
        }

        /* Danger Level Stat */
        if (statReq.getDLevel() != null) {
            statisticsResp.setDLevelStat(prisonerRepository
                    .statisticsByDLevel(statReq.getDLevel())
                    .stream()
                    .collect(Collectors.toMap((StringProjection::getString),
                            StringProjection::getCount)));
        }

        /* Marriage Status Stat */
        if (statReq.getMarriage() != null) {
            statisticsResp.setMarriageStat(prisonerRepository
                    .statisticsByMarriage(statReq.getMarriage())
                    .stream()
                    .collect(Collectors.toMap((StringProjection::getString),
                            StringProjection::getCount)));
        }

        /* Education Level Stat */
        if (statReq.getEdLevel() != null) {
            statisticsResp.setEdLevelStat(prisonerRepository
                    .statisticsByEdLevel(statReq.getEdLevel())
                    .stream()
                    .collect(Collectors.toMap((StringProjection::getString),
                            StringProjection::getCount)));
        }

        /* District Stat */
        if (statReq.getDistrict() != null) {
            statisticsResp.setDistrictStat(prisonerRepository
                    .statisticsByDistrict(statReq.getDistrict())
                    .stream()
                    .collect(Collectors.toMap((StringProjection::getString),
                            StringProjection::getCount)));
        }

        return statisticsResp;
    }

    @Override
    public List<Prisoner> findPrisonersByKeyword(String keyword, String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Prisoner> prisoners = switch (filter) {
            case "crimeType" -> prisonerRepository.findAllByCrimeTypeContaining(keyword, pageable);
            case "idType" -> prisonerRepository.findAllByIdTypeContaining(keyword.toLowerCase(), pageable);
            case "idNumber" -> prisonerRepository.findAllByIdNumberContaining(keyword.toLowerCase(), pageable);
            case "term" -> prisonerRepository.findAllByTermContaining(keyword.toLowerCase(), pageable);
            case "status" -> {
                try {
                    yield prisonerRepository.findAllByStatus(PrisonerStatus.valueOf(keyword.toUpperCase()), pageable);
                } catch (IllegalArgumentException e) {
                    String message = PRISONER_STATUS_NOT_FOUND.formatted(keyword);
                    log.error(message);
                    throw new NotFoundException(message);
                }
            }
            case "dLevel" -> prisonerRepository.findAllByDLevel(DangerLevel.valueOf(keyword.toUpperCase()), pageable);
            case "name" -> prisonerRepository.findAllByName(keyword.toLowerCase(), pageable);
            default -> throw new NotFoundException("Invalid filter");
        };

        if (prisoners.hasContent()) return prisoners.getContent();
        else return new ArrayList<>();
    }

    @Override
    @Transactional
    public Prisoner editPrisoner(int id, int deptId, Prisoner updatedPrisoner) {
        Prisoner existingPrisoner = findById(id);

        if (updatedPrisoner.getFname() != null)
            existingPrisoner.setFname(updatedPrisoner.getFname());

        if (updatedPrisoner.getLname() != null)
            existingPrisoner.setLname(updatedPrisoner.getLname());

        if (updatedPrisoner.getDob() != null)
            existingPrisoner.setDob(updatedPrisoner.getDob());

        if (updatedPrisoner.getIdNumber() != null)
            existingPrisoner.setIdNumber(updatedPrisoner.getIdNumber());

        if (updatedPrisoner.getIdType() != null)
            existingPrisoner.setIdType(updatedPrisoner.getIdType());

        if (updatedPrisoner.getAddress() != null)
            existingPrisoner.setAddress(updatedPrisoner.getAddress());

        if (updatedPrisoner.getMarriage() != null)
            existingPrisoner.setMarriage(updatedPrisoner.getMarriage());

        if (updatedPrisoner.getDLevel() != null)
            existingPrisoner.setDLevel(updatedPrisoner.getDLevel());

        if (updatedPrisoner.getCrimeType() != null)
            existingPrisoner.setCrimeType(updatedPrisoner.getCrimeType());

        if (updatedPrisoner.getStatus() != null)
            existingPrisoner.setStatus(updatedPrisoner.getStatus());

        if (updatedPrisoner.getEdLevel() != null)
            existingPrisoner.setEdLevel(updatedPrisoner.getEdLevel());

        if (updatedPrisoner.getTerm() != null)
            existingPrisoner.setTerm(updatedPrisoner.getTerm());

        if (updatedPrisoner.getWork() != null)
            existingPrisoner.setWork(updatedPrisoner.getWork());

        if (updatedPrisoner.getRelDate() != null)
            existingPrisoner.setRelDate(updatedPrisoner.getRelDate());


        // Handle relationships if necessary
        if (updatedPrisoner.getDegrees() != null && !updatedPrisoner.getDegrees().isEmpty()) {
            existingPrisoner.getDegrees().clear();
            existingPrisoner.getDegrees().addAll(updatedPrisoner.getDegrees());
        }
        if (updatedPrisoner.getCourses() != null && !updatedPrisoner.getCourses().isEmpty()) {
            existingPrisoner.getCourses().clear();
            existingPrisoner.getCourses().addAll(updatedPrisoner.getCourses());
        }
        if (updatedPrisoner.getCommunications() != null && !updatedPrisoner.getCommunications().isEmpty()) {
            existingPrisoner.getCommunications().clear();
            existingPrisoner.getCommunications().addAll(updatedPrisoner.getCommunications());
        }

        return savePrisoner(deptId, updatedPrisoner);
    }

    @Override
    @Transactional
    public List<Course> findAllCourses(int id) {
        return findById(id).getCourses()
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Degree> findAllDegrees(int id) {
        return findById(id).getDegrees()
                .stream()
                .map(PrisonerDegree::getDegree)
                .collect(Collectors.toList());
    }


    @Override
    public List<Prisoner> findReleasePrisoners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Prisoner> slice = prisonerRepository.findReleasePrisoners(pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }


    @Override
    public List<Prisoner> findAllByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Slice<Prisoner> slice = prisonerRepository.findAllByStatus(PrisonerStatus.valueOf(status), pageable);
            if (slice.hasContent()) return slice.getContent();
            else return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            String message = STATUS_NOT_FOUND.formatted(status);
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<Prisoner> findAllByDept(int deptId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Prisoner> slice = prisonerRepository.findAllByDeptId(deptId, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public List<Prisoner> findAllByRespStaff(int staffId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Prisoner> slice = prisonerRepository.findAllByRespStaffId(staffId, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public List<Communication> findAllCommunications(int id) {
        return findById(id).getCommunications();
    }

    @Override
    public Communication findCommunicationById(int id) {
        return communicationRepository.findById(id)
                .orElseThrow(() -> {
                    String message = COMMUNICATION_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    @Transactional
    public Communication addCommunication(int id, Communication communication) {
        Communication savedCommunication = communicationRepository.save(communication);
        Prisoner prisoner = findById(id);
        prisoner.getCommunications().add(savedCommunication);
        return savePrisoner(prisoner).getCommunications().stream()
                .filter(c -> c.getId().equals(savedCommunication.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    String message = COMMUNICATION_NOT_FOUND.formatted(savedCommunication.getId());
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    @Transactional
    public Prisoner deleteCommunication(int prisonerId, int commId) {
        Prisoner prisoner = findById(prisonerId);
        Communication communication = prisoner.getCommunications().stream()
                .filter(c -> c.getId().equals(commId))
                .findFirst()
                .orElseThrow(() -> {
                    String message = COMMUNICATION_NOT_FOUND.formatted(commId);
                    log.error(message);
                    return new NotFoundException(message);
                });
        prisoner.getCommunications().remove((communication));
        return savePrisoner(prisoner);
    }

    @Override
    public int numberPrisoners() {
        return prisonerRepository.countPrisoners();
    }
}
