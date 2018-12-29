package module;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class modFileList {
	private String strPath; // 파일 가져올 경로

	public String[][] fileList;

	public int tmpCnt; // 필터된 총 파일 갯수
	private int tmpCntPrs = 0; // 현재 배열 진행 번호
	public int enCnt;
	public int deCnt;

	public modFileList(String tmpPath){
		this.strPath = tmpPath; // 객체 초기 생성시부터 파일 가져올 경로 지정
	}

	public String getPath(){
		return this.strPath;
	}

	public void setPath(String tmpPath){
		this.strPath = tmpPath; // 새로운 경로로 지정
	}

	public File[] getDrive() {
		File[] roots = File.listRoots();
		return roots;
	}

	public String[][] getList() {
		enCnt=0;
		deCnt=0;
		tmpCnt = 0;
		tmpCntPrs = 0;

		fileList = null;

		try {
			cntList(strPath);
			System.out.println("[Flow _ File List Module] 지정된 디렉토리의 총 파일 갯수는  "+tmpCnt+"개 입니다.");
			fileList = new String[tmpCnt][];
			List(strPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileList;
	}

	public int getFileCount() {
		return this.tmpCnt;
	}

	// PRIVATE FUNCTIONS
	private void List(String a) throws IOException{
		File dirs = new File(a);
		String[] fileNames = dirs.list();

		for (String s : fileNames) {
			File f = new File(a + "\\" + s);
			if (f.isDirectory() == true) {
				List(f.getPath());
			}else{
				Filter(f.getPath());
			}
		}
	}

	private void Filter(String a) {
		File file = new File(a); // 날짜를 받아오기 위해 파일의경로까지 넣어주는거

		String tmpFileName = file.getName();
		String tmpFileParameter = tmpFileName.substring(tmpFileName.lastIndexOf(".")+1);
		String tmpIsEncrypt = tmpFileParameter.substring(tmpFileParameter.lastIndexOf("_")+1);

		SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 불러오기

		switch (tmpFileParameter) { // 파일의 확장자 필터링
			case "txt":
			case "xlsx":
			case "pptx":
			case "docx":
			case "ppt":
			case "doc":
			case "hwp":
			case "xls":
			case "txt_E":
			case "xlsx_E":
			case "pptx_E":
			case "docx_E":
			case "ppt_E":
			case "doc_E":
			case "hwp_E":
			case "xls_E":
				if (tmpIsEncrypt.equals("E")) enCnt++;
				else deCnt++;
				// 필터에 속하는 파일에 대해서 리스트 추가
				//System.out.println( Arrays.toString(tmpFileInfo) );
				fileList[tmpCntPrs] = new String[]{
						file.getName(),
						date2.format(file.lastModified()),
						String.valueOf(file.length()),
						file.getParent()
				};
				break;
		}
		tmpCntPrs++;
	}

	private boolean getEncryptStatus(String a) {
		File file = new File(a); // 날짜를 받아오기 위해 파일의경로까지 넣어주는거

		String tmpFileName = file.getName();
		String tmpFileParameter = tmpFileName.substring(tmpFileName.lastIndexOf(".")+1);
		String tmpIsEncrypt = tmpFileParameter.substring(tmpFileParameter.lastIndexOf("_")+1);

		SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 불러오기

		switch (tmpFileParameter) { // 파일의 확장자 필터링
			case "txt":
			case "xlsx":
			case "pptx":
			case "docx":
			case "ppt":
			case "doc":
			case "hwp":
			case "xls":
			case "txt_E":
			case "xlsx_E":
			case "pptx_E":
			case "docx_E":
			case "ppt_E":
			case "doc_E":
			case "hwp_E":
			case "xls_E":
				if(tmpIsEncrypt.equals("E")) return true;
				else return false;
		}
		return false;
	}

	private void cntList(String a) throws IOException{
		File dirs = new File(a);
		String[] fileNames = dirs.list();

		for (String s : fileNames) {
			File f = new File(a + "\\" + s);
			if (f.isDirectory() == true) {
				cntList(f.getPath());
			}else{
				cntFilter(f.getPath());
			}
			f = null;
		}
		fileNames = null;
	}

	private void cntFilter(String a) {
		File file = new File(a); // 날짜를 받아오기 위해 파일의경로까지 넣어주는거

		String tmpFileName = file.getName();
		String tmpFileParameter = tmpFileName.substring(tmpFileName.lastIndexOf(".")+1);

		switch (tmpFileParameter) { // 파일의 확장자 필터링
			case "txt":
			case "xlsx":
			case "pptx":
			case "docx":
			case "ppt":
			case "doc":
			case "hwp":
			case "xls":
			case "txt_E":
			case "xlsx_E":
			case "pptx_E":
			case "docx_E":
			case "ppt_E":
			case "doc_E":
			case "hwp_E":
			case "xls_E":
				tmpCnt++;
				break;
		}
	}   
}