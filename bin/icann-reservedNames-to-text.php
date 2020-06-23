<?php

// parse the "Reserved Names for New gTLDs" XML file and split to plain text files
$doc = new DOMDocument();
$doc->load('reserved.xml');

$registries = $doc->getElementsByTagName('registry');

for ($i = 0 ; $i < $registries->length ; $i++) {
    $registry = $registries->item($i);
    $id = $registry->getAttribute('id');
    if ('reservedNames' == $id) {
        continue;
    }

    $fh = fopen(sprintf("../S5.5-%s.txt", strtolower($id)), 'w');

    $records = $registry->getElementsByTagName('record');
    for ($j = 0 ; $j < $records->length ; $j++) {
        $label1 = $records->item($j)->getElementsByTagName('label1');
        fwrite($fh, $label1->item(0)->textContent."\n");

        $label2 = $records->item($j)->getElementsByTagName('label2');
        if (isset($label2) && $label2->item(0)->textContent != "") {
            fwrite($fh, $label2->item(0)->textContent."\n");
        }
    }


    fclose($fh);
}
