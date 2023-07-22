package com.xceptance.common.util;
/*
 * Copyright (c) 2005-2022 Xceptance Software Technologies GmbH
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


import java.text.ParseException;

import com.xceptance.common.lang.XltCharBuffer;

/**
 * The {@link CsvUtilsDecodeV4} class provides helper methods to encode and decode values to/from the CSV format.
 * This is the high performance and most efficient method. It will avoid copying data at all cost and move
 * through the cache very efficently.
 *
 * @author René Schwietzke
 *
 * @since 7.0.0
 */
public final class CsvUtilsDecodeV4
{
    /**
     * Character constant representing a comma.
     */
    private static final char COMMA = ',';

    /**
     * Character constant representing a double quote.
     */
    private static final char QUOTE_CHAR = '"';

    /**
     * Default constructor. Declared private to prevent external instantiation.
     */
    private CsvUtilsDecodeV4()
    {
    }

    /**
     * Decodes the given CSV-encoded data record and returns the plain unquoted fields.
     *
     * @param s
     *            the CSV-encoded data record
     * @return the plain fields
     */
    public static SimpleArrayList<XltCharBuffer> parse(final String s)
    {
        return parse(new SimpleArrayList<>(32), XltCharBuffer.valueOf(s), COMMA);
    }

    // our bit flags for the parser
    @SuppressWarnings("unused")
    private static final int NONE = 0;
    private static final int IN_QUOTES = 1;
    private static final int START_MODE = 2;
    @SuppressWarnings("unused")
    private static final int SEPARATOR_EXPECTED = 4;
    private static final int LAST_WAS_SEPARATOR = 8;
    private static final int MOVE_CONTENT = 32;

    /**
     * Encodes the given fields to a CSV-encoded data record using the given field separator.
     *
     * @param list a list to append to, for memory efficiency, we hand one in instead of creating our own
     * @param src the buffer to read from
     * @param fieldSeparator the field separator to use
     * @return the CSV-encoded data record
     * @throws ParseException
     */
    public static SimpleArrayList<XltCharBuffer> parse(final SimpleArrayList<XltCharBuffer> result, final XltCharBuffer src, final char fieldSeparator)
    {
        final int size = src.length();

        // the start is like a hidden separator, because we can have a quote as first one now
        int state = LAST_WAS_SEPARATOR;
        int pos = 0;
        int start = 0;

        while (pos < size)
        {
            final char c = src.charAt(pos);

            if (c == fieldSeparator)
            {
                // we saw a separator and we start a new col
                state = state | LAST_WAS_SEPARATOR;

                // we need the data
                if (start == pos)
                {
                    result.add(XltCharBuffer.EMPTY);
                }
                else
                {
                    result.add(src.viewFromTo(start, pos));
                }

                pos++;
                start = pos;

                continue;
            }
            // if we had a " we want a separator before, otherwise " is just a normal character
            if (c == QUOTE_CHAR && (state & LAST_WAS_SEPARATOR) == LAST_WAS_SEPARATOR)
            {
                state = state & ~LAST_WAS_SEPARATOR;

                // skip quotes, new start the new pos
                // in case we have
                int offsetPos = pos + 1;
                start = offsetPos;

                int offset = 0; // in case we had quoted quotes
                while (offsetPos < size)
                {
                    final char qc = src.charAt(offsetPos);

                    if (offset > 0)
                    {
                        src.put(offsetPos - offset, qc);
                    }

                    // a qute can mean the end or another quote follows
                    if (qc == QUOTE_CHAR)
                    {
                        // ok, peak if next is quote too
                        final char pa = src.peakAhead(offsetPos + 1);
                        if (pa == QUOTE_CHAR)
                        {
                            offset++;
                            offsetPos += 2;
                            continue;
                        }
                        else
                        {
                            // that is the end of the quoted section
                            result.add(src.viewFromTo(start, offsetPos - offset));
                            state = NONE;
                            offsetPos++;
                            start = offsetPos;
                            break;
                        }
                    }
                    offsetPos++;
                }
                // sync outer pos
                pos = offsetPos;

                continue;
            }

            pos++;
            state = state & ~LAST_WAS_SEPARATOR;
        }

        // There is a rest to copy
        if (start < pos)
        {
            result.add(src.viewFromTo(start, pos));
        }
        else if ((state & LAST_WAS_SEPARATOR) == LAST_WAS_SEPARATOR)
        {
            // we had a separator at the end, so we have an empty string to add
            result.add(XltCharBuffer.empty());
        }
        else if (size == 0)
        {
            // the rare case if an empty string
            result.add(XltCharBuffer.empty());
        }

        return result;
    }
}
