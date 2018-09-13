package com.sao.repository.es;

import com.sao.domain.es.EsBlog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsBlogRepositoryTest {

    @Autowired
    private EsBlogRepository esBlogRepository;
    @Before
    public void initRepositoryData(){
        //清楚所有数据
        esBlogRepository.deleteAll();
        esBlogRepository.save(new EsBlog("登鹤雀楼","王之涣的登鹤雀楼","白日依山尽，黄河入海流，欲穷千里目，更上一层楼。"));
        esBlogRepository.save(new EsBlog("相思","王维的相思","红豆生南国，春来发几枝，愿君多采撷，此物最相思。"));
        esBlogRepository.save(new EsBlog("静夜思","李白的静夜思","床前明月光，疑是地上霜，举头望明月，低头思故乡。"));
    }
    @Test
    public void findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining() {
        PageRequest pageable= new PageRequest(0,20);
        String title="思";
        String summary="相思";
        String content="相思";
        Page<EsBlog> page= esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title,summary,content,pageable);

        System.out.println("--------start--------");
        for (EsBlog blog:page.getContent()){
            System.out.println(blog.toString());
        }
        System.out.println("--------end--------");
    }
}