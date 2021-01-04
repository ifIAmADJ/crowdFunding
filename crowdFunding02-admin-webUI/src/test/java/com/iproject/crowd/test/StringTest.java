package com.iproject.crowd.test;

import com.iproject.crowd.utils.Md5Helper;
import org.junit.Test;

public class StringTest {

    @Test
    public void testMd5(){

        String source = "root";

        String encode = Md5Helper.md5(source);

        System.out.println(encode);

    }


}
