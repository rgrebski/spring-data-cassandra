/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.core.convert;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;

/**
 * Unit tests for {@link ColumnReader}.
 *
 * @author Christopher Batey
 * @author Mark Paluch
 */
@RunWith(MockitoJUnitRunner.class)
public class ColumnReaderUnitTests {

	public static final String NON_EXISTENT_COLUMN = "column_name";

	@Mock Row row;

	@Mock ColumnDefinitions columnDefinitions;

	private ColumnReader underTest;

	@Before
	public void setup() {

		when(row.getColumnDefinitions()).thenReturn(columnDefinitions);
		underTest = new ColumnReader(row);
	}

	@Test
	public void throwsIllegalArgumentExceptionIfColumnDoesNotExistByName() {

		when(columnDefinitions.getIndexOf(NON_EXISTENT_COLUMN)).thenReturn(-1);

		try {
			underTest.get(NON_EXISTENT_COLUMN);
			fail("Expected illegal argument exception");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Column does not exist in Cassandra table: " + NON_EXISTENT_COLUMN);
		}
	}

	@Test
	public void throwsIllegalArgumentExceptionIfColumnDoesNotExistByCqlIdentifier() {

		when(columnDefinitions.getIndexOf(NON_EXISTENT_COLUMN)).thenReturn(-1);

		try {
			underTest.get(CqlIdentifier.of(NON_EXISTENT_COLUMN));
			fail("Expected illegal argument exception");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Column does not exist in Cassandra table: " + NON_EXISTENT_COLUMN);
		}
	}

	@Test
	public void throwsIllegalArgumentExceptionIfColumnDoesNotExistByCqlIdentifierAndType() {

		when(columnDefinitions.getIndexOf(NON_EXISTENT_COLUMN)).thenReturn(-1);

		try {
			underTest.get(CqlIdentifier.of(NON_EXISTENT_COLUMN), String.class);
			fail("Expected illegal argument exception");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Column does not exist in Cassandra table: " + NON_EXISTENT_COLUMN);
		}
	}
}
