<?php

error_reporting(E_ALL & ~E_DEPRECATED);

// we need to be running in the top-level directory of the repository
chdir(dirname(__DIR__));

require('Net/IDNA2.php');

fwrite(STDERR, "Retrieving list of IOC, Red Cross, and IGO reserved names for new gTLDs from the ICANN website...\n");
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL,               'https://www.icann.org/sites/default/files/packages/reserved-names/ReservedNames.xml');
curl_setopt($ch, CURLOPT_RETURNTRANSFER,    true);
curl_setopt($ch, CURLOPT_RETURNTRANSFER,    true);

// ICANN's CDN provider blocks based on user agent so we need to lie:
curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:102.0) Gecko/20100101 Firefox/102.0');

$result = curl_exec($ch);
$code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
if (false === $result || 200 != $code) {
    fwrite(STDERR, sprintf("Unable to retrieve registry XML: received response code %u\n", $code));
    exit(1);
}

fwrite(STDERR, "Parsing XML registry...\n");

// parse the "Reserved Names for New gTLDs" XML file and split to plain text files
$doc = new DOMDocument;
$doc->preserveWhiteSpace = false;
$doc->loadXML($result);

$registries = $doc->getElementsByTagName('registry');

for ($i = 0 ; $i < $registries->length ; $i++) {
    $registry = $registries->item($i);

    $id = $registry->getAttribute('id');

    if ('reservedNames' == $id) {
        continue;

    } else {
        $file = sprintf('S5.5-%s.txt', strtolower($id));

        $fh = fopen($file, 'w');

        $records = $registry->getElementsByTagName('record');

        $count = 0;
        for ($j = 0 ; $j < $records->length ; $j++) {
            $label1 = $records->item($j)->getElementsByTagName('label1');
            fwrite($fh, $label1->item(0)->textContent."\n");
            $count++;

            $label2 = $records->item($j)->getElementsByTagName('label2');
            if (isset($label2) && $label2->item(0)->textContent != '') {
                fwrite($fh, $label2->item(0)->textContent."\n");
                $count++;
            }
        }

        fclose($fh);

        fwrite(STDERR, sprintf("wrote %u labels into %s\n", $count, $file));
    }
}

fwrite(STDERR, "Parsing XML registry template...\n");

$idn = new Net_IDNA2;
$doc = new DOMDocument;
$doc->formatOutput = true;

$doc->load('registry.xml.in');

$registries = $doc->getElementsByTagName('registry');

for ($i = 0 ; $i < $registries->length ; $i++) {
    $registry = $registries->item($i);

    $id = $registry->getAttribute('id');
    if ('reservedNames' == $id) {
        continue;

    } else {
        $file = $id.'.txt';

        if (!file_exists($file)) {
            fwrite(STDERR, "file '{$file}' not found\n");
            exit(1);

        } else {
            $lines = array_map('trim', file($file));

            $count = 0;
            foreach ($lines as $line) {
                $line = preg_replace('/\s*#.*/', '', $line);

                if (empty($line)) {
                    continue;

                } else {
                    $record = $doc->createElement('record');
                    $name = $doc->createElement('name', $line);

                    if (preg_match('/^xn--/', $line)) {
                        $label = $doc->createElement('label', $line);

                    } else {
                        $label = $doc->createElement('label', $idn->encode($line));

                    }

                    $count++;

                    $record->appendChild($name);
                    $record->appendChild($label);
                    $registry->appendChild($record);
                }
            }

            fwrite(STDERR, sprintf("read %u labels from %s\n", $count, $file));
        }
    }
}

fwrite(STDERR, "Writing final registry XML...\n");

$doc->save('registry.xml');

fwrite(STDERR, "Done!\n");
