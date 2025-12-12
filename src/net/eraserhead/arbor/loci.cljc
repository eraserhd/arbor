(ns net.eraserhead.arbor.loci
 (:refer-clojure :exclude [conj get update])
 (:require
  [clojure.spec.alpha :as s]))

(s/def ::id uuid?)
(s/def ::name string?)
(s/def ::parent (s/nilable ::id))
(s/def ::locus (s/keys :req [::id ::name ::parent]))

(s/def ::loci (s/map-of ::id ::locus))
(s/def ::focus (s/nilable ::id))

(defn- valid-focus? [{:keys [::loci ::focus]}]
  (if (empty? loci)
    (nil? focus)
    (contains? loci focus)))

(s/def ::db (s/and
             (s/keys :req [::loci ::focus])
             valid-focus?))

(def empty-db
  "A db with no loci in it."
  {::loci  {},
   ::focus nil})

(defn get
  "Retrieves a loci from the db by id."
  [db id]
  (get-in db [::loci id]))

(defn focused
  "Retrieves the focused loci from the db."
  [{:keys [::focus], :as db}]
  (get db focus))

(defn update
  "Apply f to the locus in the database with id.

  Preserves invariants about focus and order."
  [db id f & args]
  {:pre [(s/assert ::db db)
         (s/assert ::id id)]
   :post [(s/assert ::db %)]}
  (-> db
    (update-in [::loci id] f)
    (assoc ::focus id)))

(defn conj
  "Adds a new loci to the db and focuses it."
  [db {:keys [::id], :as locus}]
  {:pre [(s/assert ::db db)
         (s/assert ::locus locus)]}
  (update db id (constantly locus)))
