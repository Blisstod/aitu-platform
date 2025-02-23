package kz.nur.aitu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.nur.aitu.dto.*;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.ClubApplicationForm;
import kz.nur.aitu.entity.ClubApplicationRequest;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.enums.RequestStatus;
import kz.nur.aitu.enums.ResponseStatus;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.mapper.ClubApplicationMapper;
import kz.nur.aitu.repository.ClubApplicationFormRepository;
import kz.nur.aitu.repository.ClubApplicationRequestRepository;
import kz.nur.aitu.repository.ClubRepository;
import kz.nur.aitu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubApplicationService {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubApplicationFormRepository formRepository;
    @Autowired
    private ClubApplicationRequestRepository requestRepository;
    @Autowired
    private ClubApplicationMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityUtils securityUtils;

    public ClubApplicationFormDto createForm(ClubApplicationFormCreateDto dto) {
        validateJson(dto.getTemplateContent());

        Club club = clubRepository.findById(dto.getClubId())
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));
        ClubApplicationForm form = new ClubApplicationForm();
        form.setClub(club);
        form.setDeadline(dto.getDeadline());
        form.setTemplateContent(dto.getTemplateContent());

        return mapper.toDto(formRepository.save(form));
    }

    public ClubApplicationFormDto getFormById(UUID id) {
        ClubApplicationForm form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Форма не найдена!"));

        return mapper.toDto(form);
    }

    public List<ClubApplicationRequestDto> getRequestsByFormId(UUID formId) {
        List<ClubApplicationRequestDto> list = requestRepository.findByClubApplicationFormId(formId)
                .stream()
                .map(clubApplicationRequest -> mapper.toDto(clubApplicationRequest))
                .collect(Collectors.toList());
        return list;
    }

    public ClubApplicationRequestDto createRequest(ClubApplicationRequestCreateDto dto) {
        validateJson(dto.getAnswerContent());

        ClubApplicationForm form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> new ResourceNotFoundException("Форма не найдена"));
        ClubApplicationRequest request = new ClubApplicationRequest();
        request.setClubApplicationForm(form);
        request.setAnswerContent(dto.getAnswerContent());

        if (dto.getStatus() == null || dto.getStatus().describeConstable().isEmpty()){
            request.setStatus(RequestStatus.IN_REVIEW);
        }

        return mapper.toDto(requestRepository.save(request));
    }

    public ClubApplicationRequestDto respondToRequest(ClubApplicationRequestResponse dto) {
        ClubApplicationRequest request = getRequestById(dto.getRequestId());

        request.setResponse(dto.getResponse());
        request.setResponseMessage(dto.getResponseMessage());
        request.setRespondedDate(LocalDateTime.now());
        request.setStatus(RequestStatus.ANSWERED);

        return mapper.toDto(requestRepository.save(request));
    }

    public ClubApplicationRequestDto getRequestId(UUID id) {
        return mapper.toDto(getRequestById(id));
    }

    public List<ClubApplicationRequestDto> getRequestsByVisitor() {
        User user = securityUtils.getCurrentUser();
        return requestRepository.findByCreatedBy(user.getEmail()).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<ClubApplicationFormDto> getFormsByVisitor() {
        User user = securityUtils.getCurrentUser();
        return formRepository.findByCreatedBy(user.getEmail()).stream()
                .map(mapper::toDto)
                .toList();
    }

    private ClubApplicationRequest getRequestById(UUID id) {
        return requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Запрос не найден!"));
    }

    private void validateJson(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный JSON-формат шаблона формы");
        }
    }
}
