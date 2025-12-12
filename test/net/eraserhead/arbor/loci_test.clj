(ns net.eraserhead.arbor.loci-test
 (:require
  [clojure.spec.alpha :as s]
  [clojure.test :refer [deftest is]]
  [net.eraserhead.arbor.loci :as loci]))

(deftest t-empty-db
  (is (s/valid? ::loci/db loci/empty-db)))

(deftest t-add
  (let [id #uuid "dd5e99e7-5d84-4f29-8ba6-dc403aa5021a",
        locus {::loci/id id, ::loci/name "Foo"}
        singlet (loci/add
                 loci/empty-db
                 {::loci/id id, ::loci/name "Foo"})]
    (is (= id (::loci/id (loci/get singlet id)))
        "Can retrieve stored loci")))
