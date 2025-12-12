(ns net.eraserhead.arbor.webui.events
  (:require
   [net.eraserhead.arbor :as arbor]
   [net.eraserhead.arbor.loci :as loci]
   [net.eraserhead.arbor.webui.storage :as storage]
   [re-frame.core :as rf]))

(rf/reg-fx
 ::focus-control
 (fn [id]
   (js/setTimeout
    (fn []
      (when-let [ctl (.getElementById js/document id)]
        (.focus ctl)))
    0)))

(rf/reg-event-db
 ::initialize
 (fn [_ _]
   (or (storage/load-db)
       arbor/initial-state)))

(rf/reg-global-interceptor
  (rf/->interceptor
    :id :persist-db-to-localStorage
    :after (fn [context]
             (when-let [db (rf/get-effect context :db)]
               (storage/store-db db))
             context)))

(rf/reg-event-fx
 ::new-machine
 (fn [{:keys [db]} _]
   (let [id          (random-uuid)
         new-machine {::loci/id     id
                      ::loci/name   "New Machine"
                      ::loci/parent nil}]
     {:db (update db ::loci/db loci/conj new-machine)
      ::focus-control (str "machine-" id "-name")})))

(rf/reg-event-db
 ::update-machine
 (fn [app-db [_ id k v]]
   (update app-db ::loci/db loci/update id #(assoc % k v))))
