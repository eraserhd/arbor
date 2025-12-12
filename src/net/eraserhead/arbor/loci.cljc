(ns net.eraserhead.arbor.loci
 (:refer-clojure :exclude [get])
 (:require
  [clojure.spec.alpha :as s]))

(s/def ::id uuid?)
(s/def ::name string?)
(s/def ::parent (s/nilable ::id))
(s/def ::locus (s/keys :req [::id ::name] :opt [::parent]))

(s/def ::loci (s/map-of ::id ::locus))
(s/def ::focus (s/nilable ::id))
(s/def ::db (s/keys :req [::loci ::focus]))

(def empty-db
  {::loci  {},
   ::focus nil})

(defn add
  [db {:keys [::id], :as locus}]
  {:pre [(s/assert ::db db)
         (s/assert ::locus locus)]}
  (assoc-in db [::loci id] locus))

(defn get [db id]
  (get-in db [::loci id]))
