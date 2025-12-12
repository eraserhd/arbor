(ns net.eraserhead.arbor.webui.events
  (:require
   [cljs.reader]
   [net.eraserhead.arbor :as arbor]
   [net.eraserhead.arbor.loci :as loci]
   [re-frame.core :as rf]))

(defn- try-read-string [s]
  (try
   (cljs.reader/read-string s)
   (catch js/Error e
     nil)))

(rf/reg-event-db
 ::initialize
 (fn [_ _]
   (or (some-> js/localStorage
               (.getItem "db")
               try-read-string)
       arbor/initial-state)))

;; Persist app-db to localStorage all the time
(rf/reg-global-interceptor
  (rf/->interceptor
    :id :persist
    :after (fn [context]
             (when-let [db (rf/get-effect context :db)]
               (.setItem js/localStorage "db" (pr-str db)))
             context)))

(rf/reg-event-db
 ::new-machine
 (fn [app-db _]
   (let [new-machine {::loci/id (random-uuid)
                      ::loci/name "New Machine"
                      ::loci/parent nil}]
     (update app-db ::loci/db loci/conj new-machine))))

(rf/reg-event-db
 ::update-machine
 (fn [app-db [_ id k v]]
   (update app-db ::loci/db loci/update id #(assoc % k v))))
