package com.example.app.controller;

import com.example.app.domain.vo.ReplyVO;
import com.example.app.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply/*")
public class ReplyController {
    private final ReplyService replyService;

//    댓글 등록
//    브라우저에서 JSON 타입으로 데이터를 전송하고 서버에서는 댓글의 처리 결과에 따라 문자열로 결과를 리턴한다.
//    consumes : 전달받은 데이터의 타입
//    produces : 콜백함수로 결과를 전달할 때의 타입
//    @RequestBody : 전달받은 데이터를 알맞는 매개변수로 주입
//    ResponseEntity : 서버의 상태코드, 응답 메세지 등을 담을 수 있는 타입
    @PostMapping(value = "/new", consumes = "application/json", produces = "text/plain; charset=utf-8")
    public ResponseEntity<String> write(@RequestBody ReplyVO replyVO) throws UnsupportedEncodingException {
        replyService.register(replyVO);
        return new ResponseEntity<>(new String("write success".getBytes(), "UTF-8"), HttpStatus.OK);
    }

//    특정 게시글의 댓글 전체 조회
    @GetMapping("/list/{bno}")
    public List<ReplyVO> list(@PathVariable("bno") Long boardNumber){
        return replyService.showAll(boardNumber);
    }

//    댓글 수정
//    ReplyVO에 전달된 데이터 중 replyWriter가 전달되지 않았을 경우 required를 false로 변경해주고
//    Optional 객체를 사용하여 null을 검사해준다
//    @PutMapping(value = "/{rno}")
//    @PatchMapping(value = {"/{rno}", "/{rno}/{replier}"})
    @PostMapping(value = {"/{rno}", "/{rno}/{replier}"})
    public String update(@RequestBody ReplyVO replyVO, @PathVariable("rno") Long replyNumber, @PathVariable(value = "replier", required = false) String replyWriter){
        replyVO.setReplyNumber(replyNumber);
        replyVO.setReplyWriter(Optional.ofNullable(replyWriter).orElse(replyService.show(replyNumber).getReplyWriter()));
        replyService.modify(replyVO);
        return "update success";
    }

//    댓글 삭제
//    url여러개 : {중괄호}
    @DeleteMapping(value = "/{rno}")
    public void delete(@PathVariable("rno") Long replyNumber) {
        replyService.remove(replyNumber);
    }

//    특정 게시글에 작성된 댓글 개수
    @PutMapping("/{bno}")
    public int listCount(@PathVariable Long bno){
        return replyService.getTotal(bno);
    }

//    댓글 1개 조회
    @GetMapping("/{replyNumber}")
    public ReplyVO show(@PathVariable Long replyNumber){
        return replyService.show(replyNumber);
    }



}





















