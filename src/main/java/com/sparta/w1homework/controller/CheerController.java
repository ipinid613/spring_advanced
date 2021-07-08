package com.sparta.w1homework.controller;

import com.sparta.w1homework.model.Cheer;
import com.sparta.w1homework.model.CheerDetailResponse;
import com.sparta.w1homework.repository.CheerRepository;
import com.sparta.w1homework.dto.CheerRequestDto;
import com.sparta.w1homework.service.CheerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CheerController {
    private final CheerRepository cheerRepository;
    private final CheerService cheerService;

    @PostMapping("/api/cheers")
    public Cheer createCheer(@RequestBody CheerRequestDto requestDto) {
        Cheer cheer = new Cheer(requestDto);
        return cheerRepository.save(cheer);
    }

    @GetMapping("/api/cheers")
    public List<Cheer> getCheers() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        return cheerRepository.findAllByModifiedAtBetweenOrderByModifiedAtDesc(start, end);
    }

    @DeleteMapping("/api/cheers/{id}")
    public Long deleteCheer (@PathVariable Long id) {
        cheerRepository.deleteById(id);
        return id;
    }

    @PutMapping("/api/cheers/{id}")
    public Long updateCheer(@PathVariable Long id, @RequestBody CheerRequestDto requestDto) {
        cheerService.update(id, requestDto);
        return id;
    }

    @GetMapping("/api/cheers/{id}")
    //사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스 = ResponseEntity
    //detail.js의 function getPostDetail이 실행되어서
    // /api/cheers/{id}로 get요청이 오면, id값을 받아서 findById라는 이름의 메소드 실행할건데,
    // 이 메소드는 <CheerDetailResponse>에 작성자, 시간 등 사용자가 웹에서 입력한 정보를 담고, 이를 가지고 cheerService의 findByID를 실행!
    // cheerService.findById를 실행해서 반환받은 결과를 ajax통신을 통해 JSON형식으로 detail.js의 function getPostDetail에 넘겨줌!!
    public ResponseEntity<CheerDetailResponse> findById(@PathVariable Long id ) {
        return ResponseEntity.ok(cheerService.findById(id));
    }
}