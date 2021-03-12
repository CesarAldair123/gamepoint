package com.example.gamepoint.service;

import com.example.gamepoint.dto.DeveloperDto;
import com.example.gamepoint.model.Developer;
import com.example.gamepoint.repository.DeveloperRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DeveloperService {

    private DeveloperRepository developerRepository;

    @Transactional
    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll().stream()
                .map(dev -> DeveloperDto.builder()
                        .id(dev.getId())
                        .name(dev.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DeveloperDto> getDevelopersByExpression(String expression) {
        return developerRepository.findDevelopersByExpression(expression).stream()
                .map(dev -> DeveloperDto.builder()
                        .id(dev.getId())
                        .name(dev.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public DeveloperDto getDeveloperById(int id) {
        return developerRepository.findById(id)
                .map(dev -> DeveloperDto.builder()
                        .id(dev.getId())
                        .name(dev.getName())
                        .build())
                .get();
    }

    @Transactional
    public DeveloperDto getDeveloperByUsername(String name) {
        return developerRepository.findDeveloperByName(name)
                .map(dev -> DeveloperDto.builder()
                        .id(dev.getId())
                        .name(dev.getName())
                        .build())
                .get();
    }

    @Transactional
    public void updateDeveloper(DeveloperDto developerDto) {
        Developer developer = developerRepository.findById(developerDto.getId()).get();
        developer.setName(developerDto.getName());
    }

    @Transactional
    public void addDeveloper(DeveloperDto developerDto) {
        Developer developer = Developer.builder()
                .name(developerDto.getName())
                .build();
        developerRepository.save(developer);
    }
}
