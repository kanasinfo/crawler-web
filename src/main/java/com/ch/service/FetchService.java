package com.ch.service;

import com.ch.dao.ResourcesRepository;
import com.ch.model.Resource;
import com.ch.utils.FetchUtils;
import com.ch.utils.PropUtils;
import com.ch.utils.ThreadPoolKit;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;

/**
 * Created by Devid on 2016/10/29.
 */
@Service
@Transactional(readOnly = true)
public class FetchService {
    private static final Logger logger = Logger.getLogger(FetchService.class);

    @javax.annotation.Resource
    private ResourcesRepository resourcesRepository;

    @Transactional
    public void fetchList(int start, int pageSize, int count, String params) {
        try {
            for (int i = 0; i < count; i++) {
                int totalStart = start + i * pageSize;
                String url = getURL(totalStart, params);
                Document document = FetchUtils.getByUrl(url);
                Elements elements = document.select("table ol li a");
                if (elements != null) {
                    ListIterator<Element> iterator = elements.listIterator();
                    while (iterator.hasNext()) {
                        Element element = iterator.next();
                        Resource resource = saveResource(element);
                        startFetchContent(resource);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将下载任务加入到线程
     */
    private void startFetchContent(Resource resource) {
        ExecutorService threadPool = ThreadPoolKit.getFetchContentThread(Runtime.getRuntime().availableProcessors());
        threadPool.execute(new FetchContentThread(resource));
    }

    /**
     * 保存资源
     */
    @Transactional
    private Resource saveResource(Element element) {
        Resource resource = new Resource();
        resource.setTitle(element.text());
        resource.setUrl(element.attr("href"));
        logger.info("save resource: " + resource);
        return resourcesRepository.save(resource);
    }

    private String getURL(int start, String params) {
        String listUrl = PropUtils.getProp("url.list");
        return listUrl + "?" + params + "&start=" + start;
    }

    public long countUnsuccess() {
        return resourcesRepository.countByIsFetchSuccess(false);
    }

    @Transactional
    public void fetchUnsuccessList() {
        List<Resource> resources = resourcesRepository.findByIsFetchSuccess(false);
        for (Resource resource : resources) {
            startFetchContent(resource);
        }
    }

    class FetchContentThread implements Runnable {
        private Resource resource;

        public FetchContentThread(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            String url = PropUtils.getProp("url.domain") + resource.getUrl();
//            String url = "https://gefangshuai.github.io/2016/10/30/linux-server-operation/";
            try {
                Document document = FetchUtils.getByUrl(url);
                resource.setContent(document.outerHtml());
                resource.setFetchSuccess(true);
                resourcesRepository.save(resource);
            } catch (IOException e) {
                logger.info("抓取失败，url：" + url, e);
            }
        }
    }
}
