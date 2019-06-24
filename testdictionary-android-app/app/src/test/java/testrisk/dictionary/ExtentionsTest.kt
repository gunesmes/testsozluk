package testrisk.dictionary

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    private var sampleText = """
Çeşitli kaynaklardan delenerek hazırlanan [Test Eğitim 1](https://www.slideshare.net/MesutGne/test-mhendisliine-giri-eitimi-blm-1) ve [Test Eğitim 2](https://www.slideshare.net/MesutGne/test-mhendisliine-giri-eitimi-blm-2) isimli çalışmanın bir parçası olan "Test Sözlüğü" çalışmasında test mühendisliğinde sıkça karşılaşılan kelimelerin/terimlerin Türkçe karşılıklarını ve bazılarının kısa açıklamalarını bulabilirsiniz. Daha fazla bilgi için [Test Risk](http://www.testrisk.com) blog postlarını okuyabilirsiniz.

Eksik ve/veya yanlış olduğunu düşündüğünüz terimleri düzelterek veya yeni terimler ekleyerek katkı sağlamak için Github üzerinde [Test Sözlük](https://github.com/gunesmes/testsozluk) projesine (PR) istek gönderebilirsiniz. Bu sayede bu sözlüğün gelişmesine katkı sağlabilirsiniz.
    """.trimIndent()

    private var expectedText = """
Çeşitli kaynaklardan delenerek hazırlanan <a href='https://www.slideshare.net/MesutGne/test-mhendisliine-giri-eitimi-blm-1'>Test Eğitim 1</a> ve <a href='https://www.slideshare.net/MesutGne/test-mhendisliine-giri-eitimi-blm-2'>Test Eğitim 2</a> isimli çalışmanın bir parçası olan "Test Sözlüğü" çalışmasında test mühendisliğinde sıkça karşılaşılan kelimelerin/terimlerin Türkçe karşılıklarını ve bazılarının kısa açıklamalarını bulabilirsiniz. Daha fazla bilgi için <a href='http://www.testrisk.com'>Test Risk</a> blog postlarını okuyabilirsiniz.<br><br>Eksik ve/veya yanlış olduğunu düşündüğünüz terimleri düzelterek veya yeni terimler ekleyerek katkı sağlamak için Github üzerinde <a href='https://github.com/gunesmes/testsozluk'>Test Sözlük</a> projesine (PR) istek gönderebilirsiniz. Bu sayede bu sözlüğün gelişmesine katkı sağlabilirsiniz.
    """.trimIndent()

    private var sampleText2 = """
MIT License

Copyright (c) 2019 Mesut Güneş - www.testrisk.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
    """.trimIndent()

    private var expextedText2 = """
MIT License

Copyright (c) 2019 Mesut Güneş - www.testrisk.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
    """.trimIndent()


    @Test
    fun testParseTextLink() {
        assertEquals(expectedText, parseTextLink(sampleText))
    }

    @Test
    fun testRemoveNewLines() {
        assertEquals(expextedText2, removeNewLines(sampleText2))
    }
}