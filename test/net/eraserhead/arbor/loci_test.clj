(ns net.eraserhead.arbor.loci-test
 (:require
  [clojure.spec.alpha :as s]
  [clojure.test :refer [deftest is]]
  [net.eraserhead.arbor.loci :as loci]))

(deftest t-empty-db
  (is (s/valid? ::loci/db loci/empty-db)))


