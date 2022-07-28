# spec5-reserved-names

*Copyright 2013-2022 CentralNic Group PLC and contributors.*

This repository contains [a list of domain names](registry.xml) which must be reserved by gTLD Registry Operators.

Specification 5 ("Schedule of Reserved Names") of the [ICANN Registry Agreement for generic Top-Level Domains (gTLDs)](https://www.icann.org/en/registry-agreements) specifies several sets of names which must be reserved by the Registry Operator at the second level in their TLD.

The Registry Agreement does not enumerate all of these names; and ICANN has not published an authoritative list.

To mitigate the risk of confusion caused by different Registry Operators each assembling their own list, and to reduce the duplication of work which might arise, this project exists to aggregate and collate data from each of the sources referenced in Specification 5 in a format which can be easily imported into a Registry Operator's SRS database.

## Legal Notice

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## Usage

The [registry.xml](registry.xml) file in this repository is the compiled artefact that can be consumed directly. It uses the same format as ICANN's official file format (which is based on the format used by IANA for protocol registries).

To re-compile this file, run `make` in the top-level directory of the repository. You will need:

* the `make` command;
* the PHP command line executable with curl and XML support enabled;
* the [Net_IDNA2](https://pear.php.net/package/Net_IDNA2) library from PEAR or Composer;
* access to the [ICANN website](https://www.icann.org).

## Data Sources

Sources referenced in the Registry Agreementare referenced by a unique ID of the form "S5.N(.M)", where "N(.M)" corresponds to a clause from Specification 5 of the Registry Agreement.

Other sources use the appropriate unique ID, e.g. RFC number.

The list of data sources is as follows:

<table>
<tr>
<th align="right">ID</th>
<th>Description</th>
<th align="center">URL</th>
</tr>

<tr>
<td align="right">RFC.6761</td>
<td>Special-Use Domain Names</td>
<td align="center">[1]</td>
</tr>

<tr>
<td align="right">S5.1</td>
<td>Example</td>
<td align="center">-</td>
</tr>

<tr>
<td align="right" valign="top">S5.2</td>
<td>Two-character labels</td>
<td align="center">-</td>
</tr>

<tr>
<td align="right" valign="top">S5.3.1</td>
<td>Labels withheld for use in connection with the operation of the registry for the TLD</td>
<td align="center">-</td>
</tr>

<tr>
<td align="right" valign="top">S5.4.1</td>
<td>The short form (in English) of all country and territory names contained on the ISO 3166-1 list</td>
<td align="center">[2]</td>
</tr>

<tr>
<td align="right" valign="top">S5.4.2</td>
<td>The United Nations Group of Experts on Geographical Names, Technical Reference Manual for the Standardization of Geographical Names, Part III Names of Countries of the World</td>
<td align="center">[3]</td>
</tr>

<tr>
<td align="right" valign="top">S5.4.3</td>
<td>The list of United Nations member states in 6 official United Nations languages prepared by the Working Group on Country Names of the United Nations Conference on the Standardization of Geographical Names</td>
<td align="center">[4]</td>
</tr>

<tr>
<td align="right" valign="top">S5.5</td>
<td>International Olympic Committee; International Red Cross and Red Crescent Movement</td>
<td align="center">[5]</td>
</tr>

<tr>
<td align="right" valign="top">S5.6</td>
<td>Intergovernmental Organizations</td>
<td align="center">[5]</td>
</tr>
</table>

## References

1. [http://tools.ietf.org/html/rfc6761](http://tools.ietf.org/html/rfc6761)
1. [http://www.iso.org/iso/country_codes](http://www.iso.org/iso/country_codes)
1. [http://unstats.un.org/unsd/geoinfo/ungegn/docs/pubs/UNGEGN%20tech%20ref%20manual_m87_combined.pdf](http://unstats.un.org/unsd/geoinfo/ungegn/docs/pubs/UNGEGN%20tech%20ref%20manual_m87_combined.pdf)
1. [http://unstats.un.org/unsd/geoinfo/ungegn/docs/9th-uncsgn-docs/econf/9th_UNCSGN_e-conf-98-89-add1.pdf](http://unstats.un.org/unsd/geoinfo/ungegn/docs/pubs/UNGEGN%20tech%20ref%20manual_m87_combined.pdf)
1. [http://www.icann.org/en/resources/registries/reserved](http://unstats.un.org/unsd/geoinfo/ungegn/docs/pubs/UNGEGN%20tech%20ref%20manual_m87_combined.pdf)
