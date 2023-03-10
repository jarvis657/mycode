package main

import (
	"bufio"
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"time"
)

// Download IANA tzdata zone file:
// $ wget https://raw.githubusercontent.com/eggert/tz/master/zone1970.tab -O zone1970.tab

// countryZones returns a map of IANA Time Zone Database (tzdata) zone names
// by ISO 3166 2-character country code: map[country][]zone.
func countryZones(dir string) (map[string][]string, error) {
	fname := filepath.Join(dir, `zone1970.tab`)
	f, err := os.Open(fname)
	if err != nil {
		return nil, err
	}
	defer f.Close()

	countries := make(map[string][]string)
	s := bufio.NewScanner(f)
	for s.Scan() {
		line := s.Text()
		if strings.HasPrefix(line, "#") {
			continue
		}
		n := 3
		fields := strings.SplitN(line, "\t", n+1)
		if len(fields) < n {
			continue
		}
		zone := fields[2]
		for _, country := range strings.Split(fields[0], ",") {
			country = strings.ToUpper(country)
			zones := countries[country]
			zones = append(zones, zone)
			countries[country] = zones
		}
	}
	if err = s.Err(); err != nil {
		return nil, err
	}
	return countries, nil
}

func main() {
	zones, err := countryZones("")
	if err != nil {
		fmt.Fprintln(os.Stderr, err)
		return
	}

	utc := time.Now().UTC()
	for _, country := range []string{"RU", "CA"} {
		fmt.Println("MyCountry:", country)
		zones := zones[country]
		fmt.Println(utc, "UTC")
		for _, zone := range zones {
			loc, err := time.LoadLocation(zone)
			if err != nil {
				fmt.Fprintln(os.Stderr, err)
				continue
			}
			fmt.Println(utc.In(loc), zone)
		}
	}
}
