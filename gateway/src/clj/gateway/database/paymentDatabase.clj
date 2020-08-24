(ns gateway.database.paymentDatabase
(:require [clojure.java.jdbc :as j]))

(def password (slurp "/home/pedro/password.txt"))

(def mysql-db
  {:dbtype   "mysql"
   :dbname   "financeiro"
   :host     "localhost"
   :port     "3306"
   :user     "admin"
   :password ""
   :ssl true
   })

(defn- now []
  (java.util.Date.))


(defn selectScoreByCity [city]
  (j/query mysql-db ["select pontuacao
                      from clientes
                      where cidade = ?" city]))

(defn selectScoreByState [state]
  (j/query mysql-db ["select pontuacao
                      from clientes
                      where estado = ?" state])
  )

(defn insertnewclienteprincipal [cpf email brith nome]
  (j/insert! mysql-db :cliente {:cpf cpf
                                :nome nome
                                :email email
                               :aniversario brith}))

(defn insertincome [cpf profession salary income]
  (j/insert! mysql-db :renda {:cpf cpf
                              :profissao profession
                              :salario salary}))

(defn insertCredtCartInformations [cpf cardNumber password limit availableLimit]
  (j/insert! mysql-db :cartao {:cpf cpf
                               :numero cardNumber
                               :senha password
                               :limite limit
                               :valorMomento availableLimit}))
(defn insertSalary [cpf profession salary]
  (j/insert! mysql-db :renda {:cpf cpf
                              :profissao profession
                              :salario salary}))

(defn returnLimitCreditCard [cpf]
  (j/query mysql-db ["select limite
                      from cartao
                      where cpf = ?" cpf]))

(defn returnDatanewCard [cpf]
  (j/query mysql-db ["select numero, limite, senha 
                      from cartao 
                      where cpf=?" cpf]))

(defn verifyPassword [cpf password]
  (j/query mysql-db ["select senha
                   from cartao
                   where cpf = ? and senha = ? " cpf password]))

(defn subtractValueCreditCard [cpf newValue]
  (j/update! mysql-db :cartao {:limite newValue}
                              ["cpf = ?" cpf]))

(defn makePurchaseCreditCard [cpfBuyer cpfFavored price qntPlots type]
  (j/insert! mysql-db :historico_pagamento {:cpfComprador cpfBuyer
                                            :cpfFavorecido cpfFavored
                                            :valor price
                                            :parcelasQuantidade qntPlots
                                            :formaPagamento type
                                            :dataPagamento (now)}))

(defn makePurchaseSlip [cpfBuyer cpfFavored price type]
  (j/insert! mysql-db :historico_pagamento {:cpfComprador cpfBuyer
                                            :cpfFavorecido cpfFavored
                                            :valor price
                                            :formaPagamento type
                                            :dataPagamento (now)}))

(defn insertSlip [id slip]
  (j/insert! mysql-db :boletoPagamento {:idPagamento id
                                     :numero slip}))






