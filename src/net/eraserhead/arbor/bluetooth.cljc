(ns net.eraserhead.arbor.bluetooth)

(defn update-device-map
  [devices device-list]
  (let [new-ids (into #{} (map :id device-list))
        devices (->> devices
                     (remove (fn [[id _]]
                               (not (contains? new-ids id))))
                     (into {}))
        devices (->> device-list
                     (filter (comp new-ids :id))
                     (reduce (fn [devices {:keys [id], :as new-device}]
                               (assoc devices id new-device))
                             {}))]
    devices))
