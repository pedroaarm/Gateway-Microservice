(ns gateway.credit.defineCredit
  (:require
   [gateway.database.paymentDatabase :as database]
   [clojure.edn :as edn]))

(defn avarageMap [data]
  (let [totalsum (->> data
                      (map :pontuacao)
                      (reduce +))
        lengthmap (count data)]))

(defn- calculateScoreByCity [city]
  (let [dataByCity (database/selectScoreByCity city)]
    (avarageMap dataByCity)))

(defn- calculateScoreBybrith [brith])

(defn- calculateScoreByState [state]
  (let [databyState (database/selectScoreByState state)]
    (avarageMap databyState)))


(defn defineScore [city state salary]
  (let [avarageCity (future (calculateScoreByCity city))
        avarageState (calculateScoreByState state)]
    (let [resultAvarage (/ (+ avarageCity avarageState) 2)])))

(defn defineLimit [salary]
  (* (edn/read-string salary) 0.3))









