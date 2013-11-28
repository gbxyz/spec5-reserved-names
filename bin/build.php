<?php

// generate an XML registry file from plain text files using a template

require('Net/IDNA2.php');

$idn = new Net_IDNA2;

$doc = DOMDocument::load('registry.xml.in');

$registries = $doc->getElementsByTagName('registry');
for ($i = 0 ; $i < $registries->length ; $i++) {
	$registry = $registries->item($i);
	$file = $registry->getAttribute('id').'.txt';
	if (!file_exists($file)) continue;

	$lines = array_map('trim', file($file));
	foreach ($lines as $line) {
		if (empty($line) || preg_match('/#/', $line)) continue;
		$record = $doc->createElement('record');
		$name = $doc->createElement('name', $line);
		$label = $doc->createElement('label', $idn->encode($line));
		$record->appendChild($name);
		$record->appendChild($label);
		$registry->appendChild($record);
	}
}

print $doc->saveXML();
