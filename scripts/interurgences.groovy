import groovy.util.XmlParser
import groovy.util.XmlSlurper

def geojsonSante = new File("sante.geojson")
def file = new File("INTER URGENCES.kml")
def kml = new XmlSlurper().parse(file)
assert kml
assert kml.Document
assert kml.Document.Folder.size() >= 1

def folder = kml.Document.Folder[0]
assert folder.name.text().startsWith("SAU en grève")
assert folder.Placemark.size() > 0

for(placemark in folder.Placemark) {
	def geojsonStr = ',{"type":"Feature","properties":{'
	geojsonStr += '"Id":interurgence_"' + placemark.ExtendedData.Data[0].value.text() + '",'
	geojsonStr += '"Secteur":"Justice",'
	geojsonStr += '"Titre":"Urgences en grève : ' + placemark.name.text() + '",'
	geojsonStr += '"Description":"' + placemark.ExtendedData.Data[1].value.text() + '",'
	geojsonStr += '"Type":"Grève",'
	geojsonStr += '"Motif":"Hôpital en danger",'
	geojsonStr += '"Debut":"",'
	geojsonStr += '"Fin":"",'
	geojsonStr += '"Source":https://www.interurgences.fr/carte-de-france/"'
	geojsonStr += '},"geometry":{"type":"Point","coordinates":['
	geojsonStr += placemark.Point.coordinates.text().trim()
	geojsonStr += ']}}\n'
	geojsonSante << geojsonStr
}
geojsonSante << "]}"
