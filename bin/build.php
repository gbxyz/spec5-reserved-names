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

	$lines = file($file);
	foreach ($lines as $line) {
		if (preg_match('/#/', $line)) continue;
		$record = $doc->createElement('record');
		$name = $doc->createElement('name', trim($line));
		$label = $doc->createElement('label', $idn->encode(trim($line)));
		$record->appendChild($name);
		$record->appendChild($label);
		$registry->appendChild($record);
	}
}

print $doc->saveXML();
