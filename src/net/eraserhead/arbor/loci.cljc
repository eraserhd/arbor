(ns net.eraserhead.arbor.loci
 (:require
  [clojure.spec.alpha :as s]))

(s/def ::id uuid?)
(s/def ::name string?)
(s/def ::parent (s/nilable ::id))
(s/def ::locus (s/keys :req-un [::id ::parent]))

(s/def ::loci (s/map-of ::id ::node))

(s/def ::focus (s/nilable ::id))
(s/def ::db (s/keys :req-un [::focus]))

(def empty-db
  {::loci  {},
   ::focus nil})

;; add-child
;; focused-node
;; focused-path
;; focused-leaves
;; enumerate

