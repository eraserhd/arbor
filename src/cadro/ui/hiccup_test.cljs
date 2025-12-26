(ns cadro.ui.hiccup-test
  (:require
   [cadro.ui.hiccup :as h]
   [clojure.test :refer [deftest testing is]]))

(deftest t-wrap-content
  (testing "merging props"
    (is (= [:foo {} [:bar]]
           (h/wrap-content {} [:foo {} [:bar]])))))
