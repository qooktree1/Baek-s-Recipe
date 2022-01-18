# -*- coding: cp949 -*-

import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import requests
from bs4 import BeautifulSoup
from openpyxl import Workbook
import urllib.request
import re

cred = credentials.Certificate("recipe-764c2-firebase-adminsdk-57tzp-5e69d0ff4a.json")
firebase_admin.initialize_app(cred, {
  'databaseURL': 'https://recipe-764c2-default-rtdb.asia-southeast1.firebasedatabase.app/',
})

def clean(text):
    text = text.replace(',','')
    text = text.replace('.','')
    text = text.replace('[','')
    text = text.replace(']','')
    text = text.replace('?','')
    text = text.replace('#','')
    
    return text


####### 함수 정의 #######
def PageCrawler(url):
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    img_path = '#main_thumbs'

    recipe_source={}
    recipe_step=[]

    res=soup.find('div','view2_summary')
    res=res.find('h3')
    
    recipe_title = res.get_text()
    recipe_title = clean(recipe_title)

    img_url = soup.select(img_path)[0]['src']
    
    
    '''res=soup.find('div','view2_summary_info')'''

    res=soup.find('div','ready_ingre3')

    dir = db.reference(recipe_title)
    dir.update({'이미지' : img_url})

    
    try :
        source = []
        for n in res.find_all('ul'):
            
            title=n.find('b').get_text()  ## 재료 라는 텍스트 ###
            recipe_source[title]=''
            for tmp in n.find_all('li'):
                source.append(tmp.get_text().replace('\n','').replace(' ',''))
                dir=db.reference(recipe_title)
                dir.update({'재료': source})
            recipe_source[title]=source
    except (AttributeError):
        return


    '''
    try :
        for n in res.find_all('ul'):
            source = []
            title=n.find('b').get_text()  ## 재료 라는 텍스트 ###
            recipe_source[title]=''
            for tmp in n.find_all('li'):
                source.append(tmp.get_text().replace('\n','').replace(' ',''))
                dir=db.reference(recipe_title)
                dir.update({'"재료"': source})
            recipe_source[title]=source
    except (AttributeError):
        return
    '''
    
    res = soup.find('div','view_step')
    i=0
    for n in res.find_all('div','view_step_cont'):
        i=i+1
        recipe_step.append(n.get_text().replace('\n',' '))
        dir=db.reference(recipe_title)
        dir.update({'순서': recipe_step})
    
    if not recipe_step:
        return
    
    recipe_all=[recipe_title,recipe_source,recipe_step]    
    return(recipe_all)
####### 함수 정의 #######




### base 주소 ###
base = 'https://www.10000recipe.com'
### base 주소 ###


for page_num in range(5, 21):   ### 총 페이지 20개, child 번호 1~44까지 2중 for문 ###

    
    print(str(page_num) + '번째 페이지') ### 현재 페이지 번호 출력 ###
    

    ### Beautiful Soup 설정 ###
    response = requests.get("https://www.10000recipe.com/recipe/list.html?q=%EB%B0%B1%EC%A2%85%EC%9B%90%EB%A0%88%EC%8B%9C%ED%94%BC&order=reco&page="+ str(page_num))
    html_data = BeautifulSoup(response.text, 'html.parser')
    ### Beautiful Soup 설정 ###

    
    for child_num in range(1, 45):  ### 현재 페이지에 있는 url 가져오기 ###   1,45
        if child_num == 6 or child_num == 15 or child_num == 26 or child_num == 35: ###### 6, 15, 26 ,35는 페이지 없음 ######
            continue
        
        if page_num == 20: ### 마지막 페이지는 총 7개 있고 6번이 비어 있음 ###
            if child_num == 6:
                continue
            if child_num == 8:
                break
    
        text = '#contents_area_full > ul > ul > li:nth-child('+ str(child_num) + ') > div.common_sp_thumb > a'

        url = html_data.select(text)[0]['href'] ### href 속성 가져오기 ###

        final = base + url ### base에 url 붙이기 ###


        print(child_num)    ### 몇 번째 그림인지, 최종 주소 출력 ###
        print(final)
        print(PageCrawler(final))
        print('완료')
        print('\n')

        
    print('\n')
