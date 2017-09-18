/**
 * Copyright (C) 2017 Mikhail Frolov
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.truenight.quotations.util

import com.orhanobut.hawk.DefaultHawkFacade
import com.orhanobut.hawk.HawkBuilder
import rx.Observable
import rx.subjects.PublishSubject
import xyz.truenight.utils.Utils

/**
 * Copyright (C) 2017 Mikhail Frolov
 */

class RxHawkFacade(builder: HawkBuilder) : DefaultHawkFacade(builder) {

    private val subject = PublishSubject.create<Change<Any>>().toSerialized()

    override fun <T> put(key: String, value: T): Boolean {
        val put = super.put(key, value)
        @Suppress("UNCHECKED_CAST")
        val change = Change(key, value) as Change<Any>
        subject.onNext(change)
        return put
    }

    fun <T> observe(key: String): Observable<T> {
        return observe(key, null)
    }

    fun <T> observe(key: String, defValue: T?): Observable<T> {
        return Observable.merge(
                Observable.just(get<T>(key, defValue)),
                subject.filter { change -> change.same(key) }.map { change -> change.item as T }
        )
    }

    fun <T> observeChange(key: String): Observable<T> {
        return subject.filter { change -> change.same(key) }.map { change -> change.item as T }
    }

    fun notify(key: String) {
        subject.onNext(Change(key, get(key)))
    }

    class Change<T> constructor(private val key: String, val item: T?) {
        fun same(key: String): Boolean {
            return Utils.equal(this.key, key)
        }
    }
}
