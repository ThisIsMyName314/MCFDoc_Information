# Changelog

## 2.2.0 - 12th July 2026

### Added

 - New `context` annotation
 - Deprecated and hidden functions will be marked when present in the output


### Changed

- The `deprecated` annotation may now have a message
- The help message is now much more informative
- The 'Missing Message' error message now logs the function causing the error
- Multiple lines for certain annotations are now grouped more closely together
- Various changes to the output format and design
- The `showimpspecheaders` now defaults to false
- The date created is now shown in a readable format at the bottom of the output

## 2.0.2 - 17th May 2026

### Changed

- Datapack version and author fields are now below the name in the output

## 2.0.1 - 13th May 2026

### Added

- New `showimpspecheaders` flag

## 2.0.0 - 9th May 2026

### Changed

- The output format has been completely changed in order to more closely resemble JavaDoc pages
- The function summary field now has clickable hyperlinks
- Functions declared under the `minecraft` namespace are also now parsed
- Both `function` and `functions` directories are now checked
- Most references to 'tags' have been renamed to refer to 'annotations' for clarity

### Removed

- The `legacy` flag has been removed as it has become redundant
- The number of modules is no longer displayed in the output

### Fixed

- Type names do no longer have to immediately follow the `#`
- The help message now displays the correct version

### Security

- The module name is now also sanitised in the output

## 1.2.0 - 30th April 2026

### Added

- Number of modules is now stated after the function summary title

### Changed

- Functions with invalid names are now skipped

## 1.1.0 - 29th April 2026

### Added

- New `@hidden` tag
- New `showhidden` flag

## 1.0.0 - 25th April 2026

Initial Release











