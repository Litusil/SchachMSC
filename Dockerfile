FROM hseeberger/scala-sbt
WORKDIR /SchachMSC
ADD . /SchachMSC
CMD sbt run