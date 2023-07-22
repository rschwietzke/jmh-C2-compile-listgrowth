/*
 * Copyright (c) 2005-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.utils;

import java.util.List;

import org.junit.Assert;

import com.xceptance.common.lang.XltCharBuffer;
import com.xceptance.common.util.CsvUtilsDecode;

public class CSVUtilsDecodeTest extends AbstractCSVUtilsDecodeTest
{
    void test_noQuoteConversion(String s, String... expected)
    {
        final List<XltCharBuffer> result = CsvUtilsDecode.parse(s);

        Assert.assertEquals(expected.length, result.size());
        for (int i = 0; i < expected.length; i++)
        {
            Assert.assertEquals(expected[i], result.get(i).toString());
        }
    }
}
