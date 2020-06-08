#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Jun  7 23:42:42 2020

@author: mac
"""
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support import expected_conditions as EC
from lxml import etree
import time,random
import csv
from selenium.webdriver.support.wait import WebDriverWait

class spiderall(object):
    #设置一个构造器
    def __init__(self,url):
        self.url=url
        #调用spider函数
        self.spider(url)
    def spider(self,url):
        #获取浏览器
        browser=webdriver.Chrome()
        #打开地址
        browser.get(url)
        time.sleep(0.5)
        k=0

        #先找到这个这个标签
        for i in range(1,10000):
            try:
                data_center=browser.find_element_by_xpath('//ul/li['+str(1)+']/div/div['+str(i)+']/div/div[1]/h3/a').text
                f = open('/Users/mac/IdeaProjects/com.ly.sparkStream/src/main/scala/wordpython/name.txt',mode='a')
                print(data_center)
                f.write(data_center)
                f.flush()
                f.close()
                time.sleep(0.5)
            except:
                k+=1
                try:
                    load=browser.find_element_by_class_name("load_more_btn")
                    ActionChains(browser).click(load).perform()#单机数据中心
                except:
                    print("")
                #load_more_btn
                print("")
                if(k==30):
                    break
if __name__=='__main__':
    url="https://news.163.com/"
    spiderall(url)
