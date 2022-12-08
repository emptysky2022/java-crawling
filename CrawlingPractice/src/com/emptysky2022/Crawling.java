package com.emptysky2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawling {
	List<TableVO> list = new ArrayList<>();
	
	
	public static void main(String[] args) {
//		String test = "<이카루스(ICARUS)>라는 이름은 그 어떤 게임보다 높이 날아올라 최고의 게임으로 인정 받겠다는 도전과 열정을 표현함과 동시에, <이카루스>가 자랑하는 웅장한 공중 전투의 특별함을 담고 있다. 위메이드의 미래를 책임질 든든한 날개 <이카루스>는 중세 유럽 스타일의 초대형 MMORPG(Massive Multiplayer Online Role Playing Game)로 최고의 완성도를 위해 수년간 담금질을 거듭해 왔다. 아름다운 하카나스 왕국을 배경으로 펼쳐지는 긴장감 가득한 판타지 모험과 4대 주신을 도와 마신을 물리쳤던 용맹한 인간의 영웅담을 스펙터클하고도 사실감 있게 선보인다. <이카루스>는 접속과 동시에 확인할 수 있는 블록버스터 급 스케일과 미려한 그래픽, 탄탄한 구성의 스토리로 눈길을 사로잡으며, 진일보한 온라인 게임 기술력으로 구현해낸 혁신적 핵심 시스템들이 치밀해진 완성도를 보다 눈부시게 한다. 눈을 뗄 수 없을 만큼 화려하고 정교한 전투가 <이카루스>의 핵심이다. <이카루스>에 등장하는 버서커, 어쌔신, 가디언, 프리스트, 위저드 5종 클래스의 캐릭터는 각기 다른 스타일의 호쾌한 공격기를 구사하고, 상대하는 몬스터의 수많은 리액션에 따라 매 순간 적절한 전투 방식을 선택할 수 있도록 해 액션 장르 게임에서나 느낄 수 있었던 박진감과 짜릿함까지 녹여냈다. 특히, 그간 단순히 사냥의 대상으로만 여겨지던 각종 몬스터를 다양한 방법으로 활용하는 ‘펠로우 시스템’ 그리고 이 들을 길들인 후 탑승해 펼치는 ‘지상 및 공중 전투 시스템’은 기존 온라인 게임에서는 불가능했던 환상적인 경험을 이용자들에게 선사한다. 세분화된 커스터마이징 시스템을 통해 머릿속으로 그린 수백 가지의 캐릭터 외형을 만들어갈 수 있다는 점 또한 매력적이다. 얼굴 및 체형의 각 부분에 대한 세세한 옵션 선택이 가능해, 각기 다른 취향에 맞게 개성 넘치는 분신을 탄생시킨 후 <이카루스> 세계 속 주인공으로 등장시킬 수 있다. 이 외에도 ▲<이카루스>의 재미를 배가 시켜줄 방대한 시나리오 퀘스트 ▲자유로운 커뮤니티 활동의 기반이 될 세력 시스템 ▲파티원 간 협력으로 헤쳐가는 다채로운 던전 등 MMORPG의 기본기를 완벽히 갖추고 있으며, 이는 <이카루스>만의 독특한 게임성과 절묘하게 어울려 한층 진화된 MMORPG의 진수를 보여준다.";
//		String test = "Creative Arts Amusement";
//		System.out.println(test.length());
		Crawling c = new Crawling();
		c.gameOfMonth();
	}

	private void gameOfMonth() {
		TableDAO dao = TableDAO.newInstance();
		
		try {			
			Element pages = Jsoup.connect("https://www.kocca.kr/bestgame/onlinerecpt/list.do?siteId=&searchWrd=&searchCnd=&cateYears=&gameTpS=&gameLevelS=&gameGenreS=&menuNo=200481&pageIndex=1").get().select("div.paging a").last();
			int pagecnt = Integer.parseInt(pages.attr("href").substring(pages.attr("href").indexOf("pageIndex=")+10));
			
			for(int i = 1; i <= pagecnt; i++) {
				Element doc = Jsoup.connect("https://www.kocca.kr/bestgame/onlinerecpt/list.do?siteId=&searchWrd=&searchCnd=&cateYears=&gameTpS=&gameLevelS=&gameGenreS=&menuNo=200481&pageIndex=" + i).get();
				Elements hrefs = doc.select("div.gallery_list dt a");
				
				for(int j = 0; j < hrefs.size(); j++) {
					String detail = hrefs.get(j).attr("onclick");
					int pageId = Integer.parseInt(detail.split("'")[1]);
					Element subDoc = Jsoup.connect("https://www.kocca.kr/bestgame/awardedgame/view.do?nttId=" + pageId + "&menuNo=200481").get();
					String name = subDoc.select("div.viewWrap h4.tit").text();
					System.out.println("게임 이름 : " + name);
					
					
					Elements details = subDoc.select("div.viewWrap ul.detail li");
					System.out.println(details.get(0).text().split(": ").length);
					
					String section = details.get(0).text().split(" : ").length>1 ? details.get(0).text().split(": ")[1] : "없음";
					System.out.println("응모 부문 : " + section);
					
					String genre = details.get(1).text().split(" : ").length>1 ? details.get(1).text().split(": ")[1] : "없음";
					System.out.println("게임 장르 : " + genre);
					
					String rating = details.get(2).text().split(" : ").length>1 ? details.get(2).text().split(": ")[1] : "없음";
					System.out.println("게임 등급 : " + rating);
					
					String awardDate = details.get(4).text().split(" : ").length>1 ? details.get(4).text().split(": ")[1] : "없음";
					System.out.println("수상 년도 : " + awardDate);
					
					String releaseDate = details.get(5).text().split(" : ").length>1 ? details.get(5).text().split(": ")[1] : "없음";
					System.out.println("출시 년도 : " + releaseDate);
					
					String company = details.get(3).text().split(" : ").length>1 ? details.get(3).text().split(": ")[1] : "없음";
					System.out.println("제작사 : " + company);
					
					String comAddr = details.get(6).text().split(" : ").length>1 ? details.get(6).text().split(": ")[1] : "없음";
					System.out.println("회사 URL : " + comAddr);
					
					String info = subDoc.select("div.bd_point_top1").text() != null ? subDoc.select("div.bd_point_top1").text() : "없음";
					System.out.println("게임 소개 : " + info);
										
					System.out.println("\n-------------------------------------------------------------------------\n");
					
					TableVO vo = new TableVO();
					
					vo.setName(name);
					vo.setSection(section);
					vo.setGenre(genre);
					vo.setRating(rating);
					vo.setAwardDate(awardDate);
					vo.setReleaseDate(releaseDate);
					vo.setCompany(company);
					vo.setComAddr(comAddr);
					vo.setInfo(info);
					
					dao.gameOfMonthInsert(vo);
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
