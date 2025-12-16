(ns net.eraserhead.arbor.bluetooth
  (:require
   [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::address string?)

(s/def ::device (s/keys :req [::id ::name ::address]))
(s/def ::devices (s/coll-of ::device))

;; When a new device list arrives, add new devices and remove missing devices
;; from our internal device map, while keeping state of existing devices.
(defn update-device-map
  [devices device-list]
  {:pre [(s/assert ::devices device-list)]}
  (let [new-ids (into #{} (map ::id device-list))
        devices (->> devices
                     (remove (fn [[id _]]
                               (not (contains? new-ids id))))
                     (into {}))
        devices (->> device-list
                     (filter (comp new-ids ::id))
                     (reduce (fn [devices {:keys [::id], :as new-device}]
                               (assoc devices id new-device))
                             {}))]
    devices))
