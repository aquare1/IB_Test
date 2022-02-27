package com.aquare;

import com.aquare.common.JSONChange;
import com.aquare.common.ResultDTO;
import com.aquare.controller.PersonController;
import com.aquare.entity.PersonEntity;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest
{
    @Autowired
    PersonController personController;
    @Test
    public void testController() {
        ResultDTO ret = personController.postPerson("davi");
        PersonEntity person = (PersonEntity)ret.getData();
        System.out.println(JSONChange.objToJson(ret));
        ret = personController.findById(person.getId());
        System.out.println(JSONChange.objToJson(ret));
        ret = personController.delete(person.getId());
        System.out.println(JSONChange.objToJson(ret));
        ret = personController.login();
        System.out.println(JSONChange.objToJson(ret));
    }
}
