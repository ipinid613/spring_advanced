package com.sparta.w1homework.service;

import com.sparta.w1homework.model.Cheer;
import com.sparta.w1homework.model.CheerDetailResponse;
import com.sparta.w1homework.repository.CheerRepository;
import com.sparta.w1homework.dto.CheerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CheerService {
    private final CheerRepository cheerRepository;
    @Transactional
    public Long update(Long id, CheerRequestDto requestDto) {
        Cheer cheer = cheerRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        cheer.update(requestDto);
        return cheer.getId();
    }

    //CheerController에서 cheerService.findById 실행 명령이 왔으므로 아래를 실행
    //cheerService.findById는 cheerRepository.findById 실행 결과를 반환함.
    //해당 id를 가진 게시물이 있다면 해당 게시물의 전체 정보를 반환해줌!!
    @Transactional
    public CheerDetailResponse findById(Long id) {
        return cheerRepository.findById(id)
                .map(CheerDetailResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("id를 찾지 못했습니다."));
    }
}