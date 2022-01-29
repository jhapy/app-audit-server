/*
 * Copyright 2020-2020 the original author or authors from the JHapy project.
 *
 * This file is part of the JHapy project, see https://www.jhapy.org/ for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jhapy.audit.repository

import org.jhapy.audit.domain.Session
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-15
 */
interface SessionRepository : MongoRepository<Session, UUID> {
    @Query("{'\$or' : [{'username' : {\$regex : ?0, \$options: 'i'}}, {'sourceIp' : {\$regex : ?0, \$options: 'i'}}, {'jsessionId' : {\$regex : ?0, \$options: 'i'}}]}")
    fun findByCriteria(criteria: String, pageable: Pageable): Page<Session>

    @Query(
        value = "{'\$or' : [{'username' : {\$regex : ?0, \$options: 'i'}}, {'sourceIp' : {\$regex : ?0, \$options: 'i'}}, {'jsessionId' : {\$regex : ?0, \$options: 'i'}}]}",
        count = true
    )
    fun countByCriteria(criteria: String): Long
    fun getByJsessionId(jsessionId: String): Optional<Session>
}