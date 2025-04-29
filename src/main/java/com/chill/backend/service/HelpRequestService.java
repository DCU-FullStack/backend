package com.chill.backend.service;

import com.chill.backend.repository.HelpRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelpRequestService {
    private final HelpRequestRepository helpRequestRepository;

    @Autowired
    public HelpRequestService(HelpRequestRepository helpRequestRepository) {
        this.helpRequestRepository = helpRequestRepository;
    }

    public void saveHelpRequest(com.chill.backend.model.HelpRequestEntity helpRequestEntity) {
        helpRequestRepository.save(helpRequestEntity);  // 데이터베이스에 저장
    }
}
