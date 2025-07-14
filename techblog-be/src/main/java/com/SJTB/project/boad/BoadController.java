package com.SJTB.project.boad;

import com.SJTB.framework.data.ResultVo;
import com.SJTB.project.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoadController extends BaseController {

    private final BoadService boadService;

//    @GetMapping("/public/get/boad/list")
//    public ResponseEntity<ResultVo<List<BoadResponseDto>>> BoadList() {
//        return ResponseEntity.ok().body(boadService.BoadList());
//    }

    /*게시물 전체 리스트*/
    @GetMapping("/public/get/boad/list")
    public ResponseEntity<ResultVo<List<BoadResponseDto>>> BoadList() {
        return ResponseEntity.ok().body(boadService.BoadList());
    }

    /*인기 게시물 리스트*/
    @GetMapping("/public/get/boad/poplist")
    public ResultVo<List<BoadResponseDto>> BoadPopularList(){
        return boadService.BoadPopularList();
    }

    /*게시물 검색 리스트*/
    @PostMapping("/public/post/boad/srchlist")
    public ResultVo<List<BoadResponseDto>> BoadSearchList(HttpServletRequest request, @RequestBody BoadSearchRequestDto search){
        return boadService.BoadList(request, search);
    }

    /*게시물 조회*/
    @GetMapping("/public/get/boad")
    public ResultVo<BoadResponseDto> BoadRead(@RequestParam Integer boadId){
        ResultVo<BoadResponseDto> result = new ResultVo<>();
        return result;
    }

    /*???*/
    @GetMapping("/public/get/boad/etc")
    public ResultVo<BoadResponseDto> BoadReadDetailInfo(@RequestParam Integer boadId){
        ResultVo<BoadResponseDto> result = new ResultVo<>();
        return result;
    }

    /*게시물 조회(내부에서 사용)*/
    @GetMapping("/public/get/boad/detl")
    public ResultVo<BoadResponseDto> BoadRead(HttpServletRequest request, @RequestParam Integer boadid){
        return boadService.BoadViewInfo(request, boadid);
    }

    /*게시물 입력*/
    @PostMapping("/private/post/boad/inst")
    public ResultVo<BoadResponseDto> BoadInsert(HttpServletRequest request, @RequestBody BoadRequestDto boad) {
        return boadService.BoadInsert(request, boad);
    }

    /*게시물 수정*/
    @PostMapping("/private/post/boad/updt")
    public ResultVo<BoadResponseDto> BoadUpdate(HttpServletRequest request, @RequestBody BoadRequestDto boad) {
        return boadService.BoadUpdate(request, boad);
    }

    /*게시물 삭제*/
    @PostMapping("/private/post/boad/dlte")
    public ResultVo<BoadResponseDto> BoadDelete(HttpServletRequest request, @RequestBody BoadRequestDto boad) {
        return boadService.BoadDelete(request, boad);
    }

    /*게시물 조회 및 좋아요 수*/
    /*여기서 게시물 조회수 +1 메소드도 같이 동작 */
    @GetMapping("/public/get/boad/countList")
    public ResultVo<BoadCountResponseDto> BoadCountList(@RequestParam Integer boadId){
        return boadService.BoadCountList(boadId);
    }

    /*게시물 좋아요 */
    @PostMapping("/private/post/boad/like")
    public ResultVo<BoadCountResponseDto> BoadUpdateLike(HttpServletRequest request, @RequestBody BoadRequestDto boadRequestDto){
        return boadService.BoadUpdateLike(request, boadRequestDto);
    }

    /*게시물 댓글 목록*/
    @GetMapping("/public/get/cmmt/list")
    public ResultVo<List<CmmtResponseDto>> CommentList(@RequestParam Integer boadId){
        return boadService.CommentList(boadId);
    }

    /*게시물 댓글 작성*/
    @PostMapping("/private/post/cmmt/inst")
    public ResultVo<List<CmmtResponseDto>> CommentInsert(HttpServletRequest request, @RequestBody CmmtRequestDto req) {
        return boadService.CommentInsert(request, req);
    }

    /*게시물 댓글 삭제*/
    @PostMapping("/private/post/cmmt/dlte")
    public ResultVo<List<CmmtResponseDto>> CommentDelete(HttpServletRequest request, @RequestBody CmmtRequestDto req) {
        return boadService.CommentDelete(request, req);
    }


}
