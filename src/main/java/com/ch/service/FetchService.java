package com.ch.service;

import com.ch.dao.ResourcesRepository;
import com.ch.model.Resource;
import com.ch.utils.FetchUtils;
import com.ch.utils.PropUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ListIterator;

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
                        saveResource(element);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    private void saveResource(Element element) {
        Resource resource = new Resource();
        resource.setTitle(element.text());
        resource.setUrl(element.attr("href"));
        logger.info("save resource: " + resource);
        resourcesRepository.save(resource);
    }

    private String getURL(int start, String params) {
        String listUrl = PropUtils.getProp("url.list");
        return listUrl + "?" + params + "&start=" + start;
    }
}
