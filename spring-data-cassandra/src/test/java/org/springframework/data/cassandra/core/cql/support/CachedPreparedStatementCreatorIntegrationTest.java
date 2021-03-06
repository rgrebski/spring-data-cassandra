/*
 * Copyright 2017 the original author or authors.
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
package org.springframework.data.cassandra.core.cql.support;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.cassandra.test.util.AbstractKeyspaceCreatingIntegrationTest;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Integration tests for {@link CachedPreparedStatementCreator}.
 *
 * @author Mark Paluch
 */
public class CachedPreparedStatementCreatorIntegrationTest extends AbstractKeyspaceCreatingIntegrationTest {

	private static final AtomicBoolean initialized = new AtomicBoolean();

	@Before
	public void before() throws Exception {

		if (initialized.compareAndSet(false, true)) {
			getSession().execute("CREATE TABLE IF NOT EXISTS user (id text PRIMARY KEY, username text);");
		} else {
			session.execute("TRUNCATE user;");
		}
	}

	@Test // DATACASS-403
	public void shouldRetainIdempotencyFlag() {

		Insert insert = QueryBuilder.insertInto("user").value("id", bindMarker()).value("username", bindMarker());

		assertThat(insert.isIdempotent()).isTrue();

		PreparedStatementCache cache = PreparedStatementCache.create();

		PreparedStatement preparedStatement =
			CachedPreparedStatementCreator.of(cache, insert).createPreparedStatement(session);

		assertThat(preparedStatement.isIdempotent()).isTrue();
	}
}
