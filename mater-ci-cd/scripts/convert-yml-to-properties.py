import typing as t
import yaml as ya
import argparse as ap


def _parse_args() -> t.Tuple[str, str]:
    parser = ap.ArgumentParser(description="Process some script arguments.")
    parser.add_argument('-f', '--filename', default='.env', help="The environment filename.")
    parser.add_argument('-s', '--separator', default='_', help="The separator for environment file keys.")
    args: ap.Namespace = parser.parse_args()
    print(f'Parsed scripts arguments: filename={args.filename}, separator={args.separator}')
    return args.filename, args.separator


def _flatten_yaml(d: t.Any, parent_key: str = '', sep: str = '_') -> dict:
    items = []
    for k, v in d.items():
        new_key = f"{parent_key}{sep}{k}" if parent_key else k
        if isinstance(v, dict):
            items.extend(_flatten_yaml(v, new_key, sep=sep).items())
        else:
            items.append((new_key, v))
    return dict(items)


def main() -> None:
    filename, separator = _parse_args()

    with open(f'{filename}.yml', 'r') as yml_file:
        config = ya.safe_load(yml_file)

    flat_config = _flatten_yaml(config, parent_key='', sep=separator)

    with open(f'{filename}', 'w') as properties_file:
        for key, value in flat_config.items():
            properties_file.write(f"{key}={value}\n")


# --------------------------------------------------------------------------------------------------
# script executing below
main()
