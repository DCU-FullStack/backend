package com.chill.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Service
public class TableMonitorService {
    private final DataSource dataSource;
    private final SimpMessagingTemplate messagingTemplate;
    private Set<String> knownTables = new HashSet<>();

    @Autowired
    public TableMonitorService(DataSource dataSource, SimpMessagingTemplate messagingTemplate) {
        this.dataSource = dataSource;
        this.messagingTemplate = messagingTemplate;
        this.knownTables = getCurrentTables();
    }

    @Scheduled(fixedDelay = 5000)
    public void checkForNewTables() {
        Set<String> currentTables = getCurrentTables();
        if (currentTables.size() > knownTables.size()) {
            // 새로운 테이블이 생겼을 때
            alertFrontend();
        }
        knownTables = currentTables;
    }

    private Set<String> getCurrentTables() {
        Set<String> tables = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            // incidents DB에 특화된 테이블 목록을 가져오려면 여기에 catalog/schema 필터링 필요
            // 현재는 모든 테이블 목록을 가져옴
            ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    private void alertFrontend() {
        messagingTemplate.convertAndSend("/topic/alert", "NEW_TABLE_CREATED");
    }
} 