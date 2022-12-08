package com.keduit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SeoulLocationTest {
	List<SeoulLocationVO> list = new ArrayList<>();
	private String nullValue = "없음";
	
	
	public static void main(String[] args) {
		SeoulLocationTest st = new SeoulLocationTest();
		st.seoulLocation();
		
		
	}


	private void seoulLocation() {
		SeoulLocation dao = SeoulLocation.newInstance();
		
		try {
			Element pages = Jsoup.connect("https://korean.visitseoul.net/attractions").get().select("div.paging-lst a").last();
			int pageCount = Integer.parseInt(pages.attr("href").substring(pages.attr("href").indexOf("=")+1));
			
			
			for(int i = 1; i <= pageCount; i++) {
				Document doc = Jsoup.connect("https://korean.visitseoul.net/attractions?curPage=" + i).get();
				Elements thumb = doc.select("ul.article-list li.item div.thumb");
				Elements title = doc.select("ul.article-list li.item div.infor-element span.title");
				Elements comment = doc.select("ul.article-list li.item div.infor-element span.text-dot-d");
				Elements link = doc.select("ul.article-list li.item a");
				
	//			System.out.println("title = " + title.get(0).text());
	//			System.out.println(thumb);
	//			System.out.println(comment);
	//			System.out.println(link);
				
				for(int j = 0; j < thumb.size(); j++) {
					System.out.println("리스트 " + ((i-1)*8 + (j+1)));
					System.out.println(title.get(j).text());
					System.out.println(comment.get(j).text());
					
					Document docSub = Jsoup.connect("https://korean.visitseoul.net"+link.get(j).attr("href").toString()).get();
					//첫번째 목록
					Elements infoName = docSub.select("div.detail-map-infor:eq(0) dl dt");
					
					//목록에 전화번호, 이용시간 있는지 체크
					int phoneNum = infoName.eachText().indexOf("전화번호");
					int infoNum = infoName.eachText().indexOf("이용시간");
					
					//없다면 -1이 저장되므로 값 체크
					String phone = phoneNum == -1 ? nullValue : docSub.select("div.detail-map-infor:eq(0) dl dd").get(phoneNum).text();
					String info = infoNum == -1 ? nullValue : docSub.select("div.detail-map-infor:eq(0) dl dd").get(infoNum).text();
					
					//두번째 목록
					Elements mapName = docSub.select("div.detail-map-infor:eq(1) dl dt");
					
					//목록에 주소, 교통정보 있는지 체크
					int addrNum = mapName.eachText().indexOf("주소");
					int trafficNum = mapName.eachText().indexOf("교통 정보");
					
					//없다면 -1이 저장되므로 값 체크
					String address = addrNum == -1 ? nullValue : docSub.select("div.detail-map-infor:eq(1) dl dd").get(addrNum).text();
					String traffic = trafficNum == -1 ? nullValue : docSub.select("div.detail-map-infor:eq(1) dl dd").get(trafficNum).text();
					
//					Element address = docSub.selectFirst("div.detail-map-infor:eq(1) dl dd");
//					Element traffic = docSub.select("div.detail-map-infor:eq(1) dl dd").get(1);
					System.out.println("주소: " + address);
					System.out.println("전화번호: " + phone);
					System.out.println("이용시간: " + info);
					System.out.println("오시는길: " + traffic);
					System.out.println("\n-----------------------------------------------------------------------------------------------------------------------\n");
					
					SeoulLocationVO vo = new SeoulLocationVO();
					
					vo.setNo(j);
					vo.setTitle(title.get(j).text());
					vo.setTitleInfo(comment.get(j).text());
					vo.setAddress(address);
					vo.setInfo(info);
					vo.setPhone(phone);
					vo.setTraffic(traffic);
					
					dao.SeoulLocationInsert(vo);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
