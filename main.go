package main

import (
	"encoding/xml"
	"fmt"
	"io/ioutil"
	"os"
)

//Project maven pom
type Project struct {
	Dependencies []Dependency `xml:"dependencies>dependency"`
}

//Dependency dependency
type Dependency struct {
	GroupId    string `xml:"groupId"`
	ArtifactId string `xml:"artifactId"`
	Version    string `xml:"version"`
	Type       string `xml:"type"`
}

func (d *Dependency) File() string {
	return fmt.Sprintf("%s.%s", d.ArtifactId, d.Type)
}
func main() {
	xmlFile, err := os.Open("pom.xml")
	if err != nil {
		fmt.Println("Error opening file:", err)
		return
	}
	defer xmlFile.Close()
	b, _ := ioutil.ReadAll(xmlFile)

	var p Project
	xml.Unmarshal(b, &p)

	for _, dependency := range p.Dependencies {
		fmt.Printf("mvn deploy:deploy-file -DrepositoryId=third-party -Dfile=%s -DgroupId=%s -DartifactId=%s -Dversion=8.5.5.11 -Dpackaging=%s -DgeneratePom=true -DgeneratePom.description=\"FX Innovation import\" \n", dependency.File(), dependency.GroupId, dependency.ArtifactId, dependency.Type)
	}
}
