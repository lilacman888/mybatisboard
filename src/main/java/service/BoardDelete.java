package service;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;
import model.BoardBean;

public class BoardDelete implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDelete");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8"); 
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String page = request.getParameter("page");
		String board_pass = request.getParameter("board_pass");
		PrintWriter out = response.getWriter();
		BoardDAO dao = BoardDAO.getInstance();
		BoardBean old = dao.getDetail(board_num);
		
		// path값
		String path = request.getRealPath("boardupload");
		System.out.println("path : " + path);
		
		//비번 비교
		if(old.getBoard_pass().equals(board_pass)) {			// 비번 일치
			int result = dao.delete(board_num);
			if(result == 1) System.out.println("글 삭제 성공");
			// 첨부파일이 있을 경우 첨부파일 삭제
			if(old.getBoard_file() != null) {
				File file = new File(path);
				file.mkdirs();
				//boardupload 디렉토리의 모든 첨부파일을 구해오기
				File[] f = file.listFiles();
				for(int i=0; i<f.length; i++) {
					if(f[i].getName().equals(old.getBoard_file())) {
						f[i].delete();
					}
				}
			}
		} else {												// 비번 불일치
			out.print("<script>");
			out.print("alert('비밀번호가 일치하지 않습니다.');");
			out.print("history.go(-1);");
			out.print("</script>");
			out.close();
		}
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/BoardListAction.do?page="+page);
		return forward;
	}

}
