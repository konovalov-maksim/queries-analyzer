package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.entities.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

/*    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TestController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GetMapping("/test")
    public String test() {
        Session session = sessionFactory.openSession();
        try {
            String result = "";
            Query query = (Query) session.get(Query.class, 1);
            return query.getText();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return "error";
    }*/
}
