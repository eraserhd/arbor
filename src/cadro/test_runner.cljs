(ns cadro.test-runner
  "Overridden test runner to run specs.

  This is copy pasta'd because @thheller says the test data macros are weird and
  could be a problem."
  {:dev/always true}
  (:require
   [clojure.spec.alpha :as s]
   [shadow.test :as st]
   [shadow.test.env :as env]
   [shadow.dom :as dom]
   [cljs-test-display.core :as ctd]))

(defn start []
  (-> (env/get-test-data)
      (env/reset-test-data!))

  (st/run-all-tests (ctd/init! "test-root")))

(defn stop [done]
  (done))

(defn ^:export init []
  (s/check-asserts true)
  (dom/append [:div#test-root])
  (start))
