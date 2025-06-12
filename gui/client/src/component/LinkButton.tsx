type LinkButtonProps = {
  href: string;
  download?: string;
  enabled?: boolean;
  children: React.ReactNode;
};

const LinkButton = ({ href, download, enabled = true, children }: LinkButtonProps) => {
  const className = "btn btn-secondary fosscut-button button-group download-button";

  if (enabled) {
    return (
      <a className={className}
        href={href}
        download={download}>
        {children}
      </a>
    );
  }

  return (
    <button type="button" disabled className={className}>
      {children}
    </button>
  );
};

export default LinkButton;
