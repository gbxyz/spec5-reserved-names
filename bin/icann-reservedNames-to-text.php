<?php

// parse the "Reserved Names for New gTLDs" XML file and split to plain text files

$doc = DOMDocument::load('http://www.icann.org/sites/default/files/packages/reserved-names/ReservedNames.xml');

$registries = $doc->getElementsByTagName('registry');

for ($i = 0 ; $i < $registries->length ; $i++) {
	$registry = $registries->item($i);
	$id = $registry->getAttribute('id');
	if ('reservedNames' == $id) continue;

	$fh = fopen(sprintf("S5.5-%s.txt", strtolower($id)), 'w');

	$names = $registry->getElementsByTagName('name');
	for ($j = 0 ; $j < $names->length ; $j++) fwrite($fh, $names->item($j)->textContent."\n");

	fclose($fh);
}