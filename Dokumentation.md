# Verwendete Entwurfsmuster
## Model-View-Controller (Leon Westhof)
Unter dem Model-View-Controller wird ein Muster (Pattern) zur Strukturierten Softwareentwicklung verstanden. Eine genaue Zuordnung zu Architektur oder Entwurfsmuster konnte bis heute noch nicht vollzogen werden. Dieses Pattern besteht aus den Teilen Model, View und Controller. Im Model werden die darzustellenden Daten gespeichert. Die Klasse Model im Projekt enthält alle Lager (Lagerhallen und Oberlager), sowie die Transaktionsliste mit ihren Daten. Die View kommt bei der Darstellung der Benutzerdaten und der Entgegennahme von Benutzeraktionen zum Einsatz. Im Normalfall kennt die View ihren Controller und das Model. Im Projekt kennt sie das Model direkt, während der Controller über Observer erreicht wird. Damit sind die Elemente entkoppelt.
Der Controller verwaltet eine oder, wie im vorliegenden Projekt, mehrere Views. Er nimmt die getätigten Benutzeraktionen entgegen und reagiert sinngemäß. Im Projekt werden diese Verhaltensweisen über das Observer Pattern realisiert. Von der Verbindung von Controller zu Model abgesehen, werden sämtliche Informationsweitergaben über das Observer Pattern realisiert.

## Singleton (Florian Bussmann)
Das Programm „Mertens Lagerwirtschaft“ bietet u.a. eine Ansicht, die alle Buchungen des Lagersystems auflistet. Die Besonderheit dieser Ansicht, dass es zu jedem Zeitpunkt nur eine Instanz dieser Ansicht (Klasse) geben kann. Im Gegensatz zu den anderen Ansichten gäbe es hier keinen Vorteil für den Nutzer, diese Ansicht mehrfach zu öffnen. Um diese, womöglich versehentliche Aktion, für den Nutzer zu unterbinden, wurde das Singleton-Pattern benutzt. Dadurch wird bei dem Versuch das bereits offene Fenster erneut zu öffnen der Fokus auf die Ansicht gesetzt statt diese neu zu instanziieren.
Alle anderen Ansichten in „Mertens Lagerwirtschaft“ können, wie standardmäßig, mehrfach geöffnet werden. Dadurch ist es dem Nutzer beispielsweise möglich mehrere Lager parallel im Detail (Lageransicht) zu betrachten.

## Observer (Jona Stubbe)
Das Observer Pattern ist ein Design Pattern, dass für ereignisgesteuerte Anwendungen nützlich ist. Es dient dazu, den oder die Ereignisempfänger von der Ereignisquelle zu entkoppeln. In diesem Projekt wird das Observer Pattern benutzt, um das Model von View und Controller und View vom Controller zu entkoppeln. Somit hängt der Controller vom Model ab, da er an diesen Änderungen vornimmt und vom View, da er diesen erzeugt. Das Model ist von keinem der beiden anderen Einheiten abhängig und ist somit am leichtesten wiederverwendbar änderbar. Jede View ist ein Observer des Models, um Änderungen am Model durch neurendern darstellen zu können. Manche Views stellen außerdem ein Observable zur Verfügung, um Transaktionen an den Controller weiterleiten zu können.
## Strategy (Jona Stubbe)
Das Strategy-Pattern dient dazu, Nutzern einer Klasse zu erlauben, das Verhalten zu beeinflussen, ohne die Implementation zu kennen. Die veränderbaren Aspekte werden als Interfaces definiert, von denen Implementationen, je nach Bedarf des Nutzers, der Instanz zur Verfügung gestellt werden.
Ein wichtiger Einsatz des Strategy Pattern ist im Lieferungsdialog, der je nach Situation eine Zu- oder Auslieferung erstellen soll. Hier sind die austauschbaren Algorithmen die Auswahl des Vorzeichens für den Buchungswert und die Bestimmung, wieviel noch in/aus einem Lager geliefert werden kann.
 
# Benutzerhandbuch
## Lagerstruktur
Nach Start der Applikation sehen Sie eine Übersicht über die verschiedenen Lagerhierarchien, sowie den Gesamtbestand und die Gesamtkapazität. Durch klicken des Menü-Buttons können Zulieferungen und Auslieferungen erstellt werden und alle Buchungen im Überblick angezeigt werden. Mit Klick auf erste Spalte in der Tabelle können Lager mit niedriger er Hierarchiestufen, sofern die Spalte mit einem „+“ bzw. „“- versehen ist, auf- oder zugeklappt werden. Mit einem Linksklick auf die anderen Spalten wird die Lagerübersicht geöffnet. Durch einen Rechtklick kann der Name des Lagers geändert werden.

## Zulieferung/Auslieferung
Auf den Masken stellt man zuerst ein Datum (Datumsformat ISO 8601: JJJJ-MM-TT) sowie die zu verschiebenden Gesamtmenge ein. Danach kann man die einzelnen Lager im linken Verzeichnisbaum auswählen und über den Slider die Menge an zu transferierenden Waren bestimmen. Mit Hilfe des Rückgängig-Buttons können vorherige Verteilungen, die in der Mitte angezeigt werden, rückgängig bzw. mit dem Wiederherstellungsbutton wiederhergestellt werden. Nachdem alle Güter verteilt wurden kann man übernehmen. Ein Rückgängigmachen ist ab diesem Zeitpunkt nicht mehr möglich.

## Alle Buchungen
Auf dieser Maske sind alle Buchungen mit Datum und Menge dargestellt. Es lässt sich nach beiden Kriterien sortieren. Mit Klick auf ein Datum öffnet sich die Lieferungsübersicht für das ausgewählte Datum.

## Lieferungsübersicht
Die Maske zeigt für ein spezielles Datum die Mengen an Güter, die in die Lager hinein und heraustransferiert wurden. Es lässt sich nach Lager und Menge sortieren. Ein Klick auf das Lager öffnet die Lageübersicht.

## Lagerübersicht
Die Lagerübersicht zeigt den aktuellen Bestand, sowie die Kapazität des Lagers bzw. die Summe seiner Unterlager und die Buchungsübersicht mit Datum und Menge an. Ein Klick auf das Datum öffnet die Lieferungsübersicht.