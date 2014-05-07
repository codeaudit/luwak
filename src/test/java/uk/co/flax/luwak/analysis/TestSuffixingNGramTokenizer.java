package uk.co.flax.luwak.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.KeywordRepeatFilter;
import org.apache.lucene.util.Version;
import org.junit.Test;
import uk.co.flax.luwak.util.SuffixingNGramTokenFilter;

import java.io.IOException;

import static uk.co.flax.luwak.util.TokenStreamAssert.assertThat;

/**
 * Copyright (c) 2014 Lemur Consulting Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class TestSuffixingNGramTokenizer {

    Analyzer analyzer = new Analyzer() {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer source = new WhitespaceTokenizer(Version.LUCENE_50);
            TokenStream sink = new SuffixingNGramTokenFilter(new KeywordRepeatFilter(source), "XX", 1, 10);
            return new TokenStreamComponents(source, sink);
        }
    };

    @Test
    public void testTokensAreSuffixed() throws IOException {

        TokenStream ts = analyzer.tokenStream("f", "term");
        assertThat(ts)
                .nextEquals("term")
                .nextEquals("tXX")
                .nextEquals("teXX")
                .nextEquals("terXX")
                .nextEquals("termXX")
                .nextEquals("eXX")
                .nextEquals("erXX")
                .nextEquals("ermXX")
                .nextEquals("rXX")
                .nextEquals("rmXX")
                .nextEquals("mXX")
                .isExhausted();

    }
}